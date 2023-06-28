<script>
    import { createEventDispatcher } from "svelte";
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Comments from "./Comments.svelte";
    import Questions from "./Questions.svelte";
    import Rating from "./Rating.svelte";
    import Utils from "../../util/Utils";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Labels from "../../const/Labels";
    import SessionUtil from "../../util/SessionUtil";

    import logo from "../../assets/logo_white.png";

    let detail;
    export let postId;
    export let showEditPublishBtn = false;

    let showSubView = false,
        activeSubView = null,
        subViewTitle = "",
        showRatingBtn = false,
        postUserId;

    const dispatch = createEventDispatcher();

    let userId = SessionUtil.get("info", true).userId;

    function onBack() {
        showSubView = false; // to make sure to hide the comments
        dispatch("hidedetails");
        location.hash = 'home';
    }

    function onShowSubView(view, title) {
        let useTimeout = false;

        if (!showSubView) {
            useTimeout = true;
        }

        showSubView = true;
        subViewTitle = title;

        if (useTimeout) {
            setTimeout(() => {
                activeSubView = view;
            }, 1000);
        } else {
            activeSubView = view;
        }
    }

    function onComments() {
        onShowSubView(Comments, Labels.details.comment_title);
    }

    function onRating() {
        onShowSubView(Rating, Labels.details.rating_title);
    }

    function onQuestions() {
        onShowSubView(Questions, Labels.details.question_title);
    }

    function onEdit() {
        showSubView = false; // to make sure to hide the comments
        Utils.redirectTo("publish", { postId });
    }

    function closeSubView() {
        showSubView = false;
    }

    function getReplies(answer) {
        let str = '';

        for(let i = 0; i < answer.length; i++){
            str += `<div class="reply-item">${answer[i].answer} 
            <div class="reply-item-auth">${answer[i].user.firstName + ' ' + answer[i].user.lastName}</div>
            </div>`;
        }

        return str;
    }

    function getQuestStr(content) {
        let str = `<div class="coll-body"> 
                <div  class="coll-quest">${content.questions || ''}
                    <div class="coll-quest-auth">${content.user.firstName + ' ' + content.user.lastName || ''}</div>
                </div>
                <div class="replies">${getReplies(content.answer)}</div>
            </div>`;

        return str;
    }

    function getContentStr(content, collabType) {
        if(Utils.isEmpty(content)) {
            return '';
        }

        content = content[0];

        if(collabType === 'cq') {
            return getQuestStr(content);
        }
        return content.questions || content.content || '';
    }

    function onCollaborateClick(e){
        let el = e.target,
            classList = el.classList,
            showDetails = classList.contains('collaborated-content');

        if(!showDetails) {
            return;
        }

        let cData = classList[1].split('-'),
            collabType = cData[0];

        if(collabType) {
            let url = urlConst.get_comment_by_id;

           if(collabType === 'cq') {
                url = urlConst.get_post_question_by_id;
           }

           url = url.replace('{postId}', postId).replace('{commentId}', cData[1]).replace('{qnId}', cData[1]);

           Request.get(url, null, 
            (res) => {
                Utils.alert(getContentStr(res.content, collabType), Labels.collaborate[collabType !== 'cq' ? 'comment_title' : 'question_title']);
            },
            (err) => {
                Utils.alert(Labels.collaborate.failed, Labels.alert.error);
            },
            onCollaborateClick
           );
        }
    }

    $: {
        if (!Utils.isEmpty(postId)) {
            Utils.mask(true);
            Request.get(
                urlConst.get_post_by_id.replace("{postId}", postId),
                null,
                (resp) => {
                    postUserId = resp.user.userId;

                    if (postUserId !== userId) {
                        showRatingBtn = true;
                    }

                    detail = resp;
                    Utils.mask();
                    Utils.log("[Get Post Details] Showing post details");
                },
                (error) => {
                    Utils.mask();
                    Utils.log(
                        "[Get Post Details] Getting error while post details"
                    );
                },
                onBack
            );
        }
    }
</script>

<div
    class="feed-details"
    transition:fly={{ x: window.innerWidth, duration: 200 }}
>
    <Toolbar cls="theme-bg">
        <div slot="left">
            <img
                src={logo}
                alt={Labels.app.name}
                class="header-logo flex-cont vh-center pl-1"
            />
        </div>

        <!-- <div slot="center" class="title">Title</div> -->

        <div slot="right" class="flex-cont">
            {#if showEditPublishBtn}
                <Button
                    iconCls="material-icons"
                    iconText="edit"
                    on:click={onEdit}
                />
            {:else}
                <Button
                    iconCls=""
                    iconText=""
                />
            {/if}
        </div>
    </Toolbar>

    {#if detail}
        <div class="flex-vh flex-dir-column feed-details-body">
            <div class="thumb-title mob-breadcrumb-cont">
                <Button
                    text= {Labels.dashboard.back}
                    iconCls="material-icons"
                    iconText="chevron_left"
                    cls="details-back"
                    on:click={onBack}
                />
             / <span class="mob-thumb-title-cont"> {detail.title} </span></div> 
            <div class="thumb-autho" on:click={onCollaborateClick}>{@html detail.content}</div>
        </div>
    {/if}
</div>

{#if showSubView}
    <div
        transition:fly={{ y: window.innerHeight / 2, duration: 1000 }}
        class="active-view"
    >
        <Toolbar cls="sub-title">
            <div slot="left" class="active-title">{subViewTitle}</div>
            <Button
                slot="right"
                ui="action"
                iconCls="material-icons"
                iconText="close"
                on:click={closeSubView}
            />
        </Toolbar>
        <svelte:component this={activeSubView} {postId} {postUserId} details={detail}/>
        <!-- <Comments {postId} /> -->
    </div>
{/if}

<div class="mob-side-menu mob-action-btn">
    <div class="flex-cont">
            <div
                class="menu-item flex-cont flex-dir-column flex-1"
                on:click={onComments}
            >
                <span class="material-icons menu-item-icon">add_comment</span>
                <span class="menu-item-text">Comments</span>
            </div>
            <div
                class="menu-item flex-cont flex-dir-column flex-1"
                on:click={onQuestions}
            >
                <span class="material-icons menu-item-icon">live_help</span>
                <span class="menu-item-text">Questions</span>
            </div>
        {#if showRatingBtn}
            <div
                class="menu-item flex-cont flex-dir-column flex-1"
                on:click={onRating}
            >
                <span class="material-icons menu-item-icon">thumbs_up_down</span>
                <span class="menu-item-text">Rating</span>
            </div>
        {/if}
    </div>
</div>

<style>
    .feed-details {
        position: absolute;
        top: 0;
        left: 0;
        height: calc(100% - 64px);
        width: 100%;
        background-color: #fff;
    }

    .active-view {
        position: absolute;
        bottom: 64px;
        z-index: 2;
        width: 100%;
        height: 70%;
        background: #fff;
        overflow: hidden;
        -webkit-box-shadow: 0px -8px 50px -15px rgba(0, 0, 0, 0.75);
        -moz-box-shadow: 0px -8px 50px -15px rgba(0, 0, 0, 0.75);
        box-shadow: 0px -8px 50px -15px rgba(0, 0, 0, 0.75);
    }

    .feed-details-body {
        max-width: 100%;
        margin: 0 auto;
        padding: 4px 2%;
        height: inherit;
        overflow-y: auto;
    }
    .active-title {
        color: var(--text-color);
        font-weight: 500;
    }
    :global(.sub-title) {
        border-bottom: 1px solid var(--nav-icon-color);
    }
    :global(.btn-el.details-back) {
        padding-left: 0;
    }

    :global(.mob-action-btn) {
        position: fixed;
        bottom: 0;
        left: 0;
        z-index: 2 !important;
    }

    :global(.mob-breadcrumb-cont) {
        font-size: 15px;
        margin-top: 6px;
    }

    :global(.mob-breadcrumb-cont .btn-container) {
        display: inline-block;
        vertical-align: middle;
        padding: 0 8px 0 0;
    }

    :global(.mob-breadcrumb-cont .btn-container .btn-el) {
        background: none;
        color: var(--blue-shade-2);
        cursor: pointer;
        padding: 0;
    }

    :global(.mob-breadcrumb-cont .mob-thumb-title-cont) {
        display: inline-block;
        vertical-align: middle;
        margin-left: 6px;
    }
</style>
