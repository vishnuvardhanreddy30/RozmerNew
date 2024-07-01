<script>
    import { onMount, createEventDispatcher } from "svelte";
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import SegmentedButton from "../../widget/button/SegmentedButton.svelte";
    import Comments from "./Comments.svelte";
    import Rating from "./Rating.svelte";
    import Questions from "./Questions.svelte";
    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    import Button from "../../widget/button/Button.svelte";
    import Boot from "../../util/Boot";
    import SessionUtil from "../../util/SessionUtil";
    import Share from '../../widget/share.svelte';

    import logo from "../../assets/logo_white.png";

    export let detail = {
        title: "",
        content: "",
    };

    export let postId = null;
    export let showEditPublishBtn = false;

    let segmentActiveView = Comments,
        detailsEl,
        detailsElHeight,
        postUserId;

    const dispatch = createEventDispatcher();

    let buttons = [
        {
            text: Labels.details.comment_title,
            pressed: true,
        },
        {
            text: Labels.details.question_title,
        },
        {
            text: Labels.details.rating_title,
        },
    ];

    let viewMap = [Comments, Questions, Rating];

    let userId = SessionUtil.get("info", true).userId;

    function onBack() {
        dispatch("hidedetails");
        const path = window.location;
        location.hash = path?.hash?.includes('articles') || path?.hash?.includes('poems') || path?.hash?.includes('home') ? Utils.getHash() : 'mypost';
        // Utils.redirectTo("home");
    }

    function onSegmentBtnSelect(e) {
        let data = e.detail;

        segmentActiveView = viewMap[data.itemId];
    }

    function onEdit() {
        Utils.redirectTo("publish", { postId });
    }

    function takeBacktoHome(){
        dispatch('hidedetailpopup');
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
                </div>
            `;

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
                    Utils.mask();
                    Utils.log("[Get Post Details] Showing post details");

                    // user can not rate on his own post
                    if (userId !== resp.user.userId) {
                        buttons = [
                            {
                                text: Labels.details.comment_title,
                                pressed: true,
                            },
                            {
                                text: Labels.details.question_title,
                            },
                            {
                                text: Labels.details.rating_title,
                            },
                        ];
                    }

                    postUserId = resp.user.userId;

                    detail = resp;
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

    onMount(() => {
        detailsElHeight = Utils.calculateAvailableSpace(detailsEl);
    });
    let isModalOpen = false;

  function openModal() {
    isModalOpen = true;
  }

  function closeModal() {
    isModalOpen = false;
  }
</script>
<Share
    title="Share post"
    isOpen={isModalOpen}
    onClose={closeModal}
    postData = {detail}
    />
<div
    class="feed-details wh-100-percent"
>
<!-- <div
    class="feed-details wh-100-percent"
    transition:fly={{ x: window.innerWidth, duration: 500 }}
> -->
    <!-- <Toolbar cls="theme-bg">
        <div slot="left" class="title">
            <img
                src={logo}
                alt={Labels.app.name}
                class="header-logo flex-cont vh-center pl-1"
                on:click={takeBacktoHome}
            />
        </div>
        <div slot="right">
            {#if showEditPublishBtn}
                <Button
                    iconCls="material-icons"
                    iconText="edit"
                    cls="edit-article-btn"
                    on:click={onEdit}
                />
            {/if}
        </div>
    </Toolbar> -->

    <div class="flex-cont">
        <div class="flex-cont flex-dir-column feed-details-body flex-1">
            <div class="breadcrumb-cont pr-2 d-flex justify-content-between">
                <span class="back-btn" on:click={onBack}>
                    <i class="material-icons small">chevron_left</i> {Labels.dashboard.back}
                </span>
                {#if showEditPublishBtn}
                    <Button
                        iconCls="material-icons"
                        iconText="edit"
                        cls="edit-article-btn"
                        on:click={onEdit}
                    />
                {/if}
                
                <div class="share-btn">
                    <i class="fa fa-share pointer" on:click={openModal}></i>
                </div>
                <!-- / -->
                <!-- <span class="breadcrumb-thumb-title">{detail.title}</span> -->
            </div>

            <div
                class="thumb-autho overflow-y ke-wrapper-wysiwyg kothing-editor-editable"
                style="height:{detailsElHeight}"
                bind:this={detailsEl}
            >
                <div class="thumb-title">{detail.title}</div>
                <div on:click={onCollaborateClick}>
                    {@html detail.content}
                </div>

                <!-- <Button
                    iconCls="material-icons"
                    iconText="Edit Feed"
                    floating="true"
                    on:click={onEdit}
                /> -->
            </div>
        </div>

        {#if Boot.isDesktop()}
            <div class="f1 feed-seg-cont">
                <SegmentedButton {buttons} on:select={onSegmentBtnSelect} />
                <svelte:component
                    this={segmentActiveView}
                    {postId}
                    {postUserId}
                    details={detail}
                />
            </div>
        {/if}
    </div>
</div>

<style>
    .feed-details {
        position: absolute;
        top: 0;
        left: 0;
        /* background-color: #fff; */
        background-color: var(--body-bg-color);
        /* z-index: 2; */
        border-radius: 10px;
    }

    .feed-details-body {
        height: calc(100vh - 54px);
        background: transparent  !important;
        border: 1px solid var(--primary-color-alternate-2);
        border-radius: 10px;
        height: 78vh;
    }

    .share-btn{
        margin-right: 10px;
    }

    .back-btn{
        font-size: 16px;
        font-weight: bold;
        color: var(--primary-color-alternate-2);
        cursor: pointer;
        padding-left: 10px;
        margin: auto 0;
    }

    .breadcrumb-cont {
        width: 100%;
        margin-top: 20px;
        border-radius: 10px !important;
        background-color: transparent !important;
    }
    .thumb-autho{
        border-radius: 10px;
        background-color: transparent !important;
    }

    .breadcrumb-cont button {
        background: none;
        border: none;
        color: var(--blue-shade-2);
        cursor: pointer;
    }

    .breadcrumb-cont button i {
        vertical-align: middle;
        padding-bottom: 2px;
    }

    .breadcrumb-cont button {
        font-size: 14px;
    }

    .thumb-title {
        font-size: 1.5rem;
        font-weight: 600;
        text-align: center;
    }

    .feed-seg-cont {
        padding: 10px;
        background-color: var(--nav-menu-selected-bg);
        width: 400px;
        border: 1px solid var(--primary-color-alternate-2);
        border-radius: 10px;
        margin-left: 10px;
    }

    :global(.feed-seg-cont .segmented-btn-cont) {
        border-bottom: 2px solid var(--border-nav);
        margin-bottom: 20px;
    }

    :global(.feed-seg-cont .segmented-btn-cont .btn-container .btn-el) {
        background-color: transparent;
        color: var(--font-color);
    }

    :global(.feed-seg-cont .segmented-btn-cont .btn-container.pressed) {
        border-bottom: 4px solid var(--primary-color-alternate-2);
    }

    :global(.feed-seg-cont .segmented-btn-cont .btn-container .ripple:hover) {
        background: none;
    }

    :global(.edit-article-btn::before) {
        content: "Edit Article";
        margin-right: 6px;
    }

    :global(.edit-article-btn .material-icons) {
        font-size: 18px;
    } 
</style>
