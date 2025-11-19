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
    import axios from "axios";

    let detail;
    export let postId;
    export let showEditPublishBtn = false;

    let showSubView = false,
        activeSubView = null,
        subViewTitle = "",
        showRatingBtn = true,
        postUserId;

    const dispatch = createEventDispatcher();

    let userId = SessionUtil.get("info", true).userId;
    // Access functionality
    let isBalanceModalOpen = false;
    let modalMessage = "Insufficient balance. Please recharge to access this artcile.";
    let isAccessModalOpen = false;
    let unlockModalMessage = "You don't have access to this post. Do you want to unlock the post.?";
    let category = Utils.getHash();

    function onBack() {
        showSubView = false; // to make sure to hide the comments
        dispatch("hidedetails");
        const path = window.location;
        location.hash = path?.hash?.includes('articles') || path?.hash?.includes('poems') || path?.hash?.includes('home') ? Utils.getHash() : 'mypost';
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
                urlConst.get_post_by_id.replace("{postId}", postId).replace("{userId}", userId),
                null,
                (resp) => {
                    postUserId = resp.user.userId;

                    // if (postUserId !== userId) {
                    //     showRatingBtn = true;
                    // }

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

    let truncatedHTML = '';
    let showFull = false;
    let totalWordsCount = 0

    $: if (detail?.content) {
        const tempElement = document.createElement('div');
        tempElement.innerHTML = detail.content;

        const allText = tempElement.textContent || '';
        const allWords = allText.match(/\b[\w']+\b/g) || [];
        const targetWordCount = Math.ceil(allWords.length * 0.2);
        totalWordsCount = allWords.length;

        let currentCount = 0;

        function truncateNode(node) {
            if (currentCount >= targetWordCount) return '';

            if (node.nodeType === Node.TEXT_NODE) {
                const words = node.textContent.match(/\b[\w']+\b/g) || [];
                if (currentCount + words.length <= targetWordCount) {
                    currentCount += words.length;
                    return node.textContent;
                } else {
                    const remaining = targetWordCount - currentCount;
                    currentCount = targetWordCount;
                    const text = words.slice(0, remaining).join(' ');
                    return text + ' ';
                }
            }
        

            if (node.nodeType === Node.ELEMENT_NODE) {
                const tag = node.tagName.toLowerCase();
                let innerHTML = '';

                node.childNodes.forEach(child => {
                    innerHTML += truncateNode(child);
                });

                return `<${tag}${getAttributes(node)}>${innerHTML}</${tag}>`;
            }

            return '';
        }

        function getAttributes(node) {
            if (!node.attributes || node.attributes.length === 0) return '';
            return Array.from(node.attributes)
                .map(attr => ` ${attr.name}="${attr.value}"`)
                .join('');
        }

        tempElement.childNodes.forEach(child => {
            truncatedHTML += truncateNode(child);
        });
        truncatedHTML = truncatedHTML

        console.log("after 20%html : ", truncatedHTML);
    }

    function readMoreArticle() {
        console.log("readmore article clicked", detail, userId)
        if (!detail.hasAccess && detail?.user && detail?.user?.userId != userId) {
            // Logic to open the payment modal goes here
            isAccessModalOpen = true;
        } else {
            showFull = true
        }
    }
    function unlockPost() {
        isAccessModalOpen = false;
        axios.post(
            urlConst.unlock_post.replace('{postId}', detail.postId).replace('{loginUserId}', userId),
            {},
            {
                headers: Request.getHeaders(null),
                timeout: 120000
            }
            )
            .then(function (response) {
                console.log("Response after unlock:", response);
                if (response.data === "Unlocked" || response.data === "Already unlocked") {
                    showFull = true;
                } else {
                    openBalanceModal();
                }
            })
            .catch(function (err) {
                if (err.response?.data?.message === "Insufficient coins" || err.response?.data?.message?.includes("Wallet not found")) {
                    openBalanceModal();
                } else {
                    Utils.log(err.response);
                }
            });
    }
    function openBalanceModal() {
        isBalanceModalOpen = true;
    }

    function closeBalanceModal() {
        isBalanceModalOpen = false;
    }
    function closeAccessModal() {
        isAccessModalOpen = false;
    }

    function navigateToPayments() {
        closeBalanceModal();
        Utils.redirectTo('payment'); // Replace 'payments' with your actual payments page route
    }

</script>

<div
    class="feed-details"
    transition:fly={{ x: window.innerWidth, duration: 200 }}
>
    <!-- <Toolbar cls="theme-bg">
        <div slot="left">
            <img
                src={logo}
                alt={Labels.app.name}
                class="header-logo flex-cont vh-center pl-1"
            />
        </div>


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
    </Toolbar> -->

    {#if detail}
        <div class="flex-vh flex-dir-column feed-details-body">
            <div class="breadcrumb-cont mob-breadcrumb-cont">
                <span class="back-btn" on:click={onBack}>
                    <i class="material-icons small">chevron_left</i> {Labels.dashboard.back}
                </span>
                <!-- <Button
                    text= {Labels.dashboard.back}
                    iconCls="material-icons"
                    iconText="chevron_left"
                    cls="details-back"
                    on:click={onBack}
                /> -->
             <!-- / <span class="mob-thumb-title-cont"> {detail.title} </span> -->
            </div> 
            <!-- <div class="thumb-autho" on:click={onCollaborateClick}>{@html detail.content}</div> -->
            <div
                class="thumb-autho overflow-y ke-wrapper-wysiwyg kothing-editor-editable"
            >
                <div class="thumb-title">{detail.title}</div>
                {#if detail.user?.userId != userId && category != 'poems'}
                    {#if totalWordsCount > 50 && !detail.hasAccess}
                        <div on:click={onCollaborateClick}>
                            {@html showFull ? detail.content : truncatedHTML}
                        </div>
                        {#if !showFull}
                            <button class="read-more-btn" on:click={() => readMoreArticle()}>Read more...</button>
                        {/if}
                    {:else}
                    <div on:click={onCollaborateClick}>
                        {@html detail.content}
                        </div>
                    {/if}
                {:else}
                <div on:click={onCollaborateClick}>
                    {@html detail.content}   
                    </div>
                {/if}
            </div>
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
{#if isBalanceModalOpen}
<div class="payment-modal-backdrop">
    <div class="payment-modal">
        <p>{modalMessage}</p>
        <button on:click={navigateToPayments}>Payment</button>
        <button on:click={closeBalanceModal}>Close</button>
    </div>
</div>
{/if}
{#if isAccessModalOpen}
<div class="payment-modal-backdrop">
    <div class="payment-modal">
        <p>{unlockModalMessage}</p>
        <button on:click={unlockPost}>Yes, Unlock</button>
        <button on:click={closeAccessModal}>Cancel</button>
    </div>
</div>
{/if}
<style>
    .feed-details {
        position: absolute;
        top: 0;
        left: 0;
        /* background-color: #fff; */
        background-color: var(--body-bg-color);
        /* z-index: 2; */
        width: 100%;
        height: 100%;
        border-radius: 10px;
    }

    .feed-details-body {
        height: calc(100vh - 54px);
        background: transparent  !important;
        border: 1px solid var(--primary-color-alternate-2);
        border-radius: 10px;
        height: 78vh;
    }

    .back-btn{
        font-size: 16px;
        font-weight: bold;
        color: var(--primary-color-alternate-2);
        cursor: pointer;
        padding-left: 10px;
        margin: auto 0;
    }

    .thumb-autho{
        border-radius: 10px;
        background-color: transparent !important;
    }
    .thumb-title {
        font-size: 1.5rem;
        font-weight: 600;
        text-align: center;
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
        height: calc(100vh - 54px);
        background: transparent  !important;
        border: 1px solid var(--primary-color-alternate-2);
        border-radius: 10px;
        height: 78vh;
    }
    .read-more-btn {
        color: blue;
        background: none;
        border: none;
        outline: none;
        cursor: pointer;
        padding: 0;
        font-size: 16px;
    }

    .read-more-btn:hover {
        text-decoration: underline;
    }

    .payment-modal-backdrop {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        z-index: 5;
        justify-content: center;
        align-items: center;
    }

    .payment-modal {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        text-align: center;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    }

    .payment-modal button {
        margin: 10px;
        padding: 10px 20px;
        cursor: pointer;
        border: none;
        border-radius: 4px;
    }

    .payment-modal button:first-child {
        background-color: #1a9b97;
        color: white;
    }

    .modal button:last-child {
        background-color: #ddd;
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
