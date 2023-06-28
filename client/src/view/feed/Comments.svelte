<script>
    import { onMount } from "svelte";

    import InfiniteLoading from "../../widget/tinylist/InfiniteLoading.svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import Utils from "../../util/Utils";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Labels from "../../const/Labels";
    import SessionUtil from "../../util/SessionUtil";
    import TIME_SINCE from "../../util/Time";
    import Base from "../../util/Base";
    import likeIcon from "../../assets/rating/like.png";
    import dislikeIcon from "../../assets/rating/dislike.png";
    import awesomeIcon from "../../assets/rating/ok.png";

    export let postId = "";
    export let postUserId = null;
    export let details;
    
    let value = 0;
    let valueDisplayed = "Cool";
    let valueIcon = likeIcon;
    let maxLength = 999;

    let noResultsText = Labels.list.comment_no_results,
        cmpEl,
        commentId,
        commentValue;

    onMount(() => {
        handleResize();
    });

    const api = urlConst.get_all_comments.replace("{postId}", postId);

    let userId = SessionUtil.get("info", true).userId;
    let page = 0;
    let list = [];

    function infiniteHandler({ detail: { loaded, complete } }) {
        Request.get(
            `${api}?pageNumber=${page}&pageSize=500&sortBy=postId&sortDir=asc`,
            null,
            (data) => {
                if (!Utils.isEmpty(data) && !Utils.isEmpty(data.content)) {
                    page += 1;
                    list = [...data.content, ...list];
                    if (list.length >= data.totalRecords) {
                        complete();
                    } else {
                        loaded();
                    }

                    noResultsText = data.content.length
                        ? ""
                        : Labels.list.comment_no_results;
                    // complete();
                } else {
                    complete();
                }

                noResultsText = data.content.length
                    ? ""
                    : Labels.list.comment_no_results;
            },
            (err) => {
                complete();
                Utils.log(err);
            },
            onSend
        );
    }

    function onSend() {
        //if user submits without entering any comment
        if(!commentValue) {
            return;
        }

        Request.post(
            urlConst.post_comments
                .replace("{postId}", postId)
                .replace("{userId}", userId),
            {
                content: commentValue,
            },
            (res) => {
                Utils.log(res);
                list = [...list, ...[res]];
                setTimeout(() => {
                    cmpEl.lastChild.scrollIntoViewIfNeeded();
                }, 100);
                noResultsText = list.length
                    ? ""
                    : Labels.list.comment_no_results;
            },
            (err) => {
                Utils.log(err);
            },
            onSend
        );

        commentValue = "";
    }

    function collaborate(e) {
        let id = e.currentTarget.getAttribute("itemId");
        
        Utils.confirm(
            Labels.collaborate.collaborate_confirm_msg,
            Labels.collaborate.collaborate_title,
            function (btn) {
                if (btn === "ok") {
                    Base.toast('info', Labels.collaborate.collaborate_body, 3000);

                    let commentData = list[getCommentIndex(+id.slice(7))] || {};

                    window['collaborateDetails'] = {
                        postId: postId,
                        details: details,
                        commentId: commentData.id
                        // commentText: commentData.content || ''
                    }
                    Utils.redirectTo('collaborate');
                }
            }
        );
    }

    function getCommentIndex(id) {
        for (let i = 0; i < list.length; i++) {
            if (list[i].id === id) {
                return i;
            }
        }
    }

    function deleteComment(e) {
        let id = e.currentTarget.getAttribute("itemId");
        
        Utils.confirm(
            Labels.list.comment_delete_confirm,
            Labels.list.comment_delete_title,
            function (btn) {
                if (btn === "ok") {
                    Request.delete(
                        urlConst.delete_comment.replace("{commentId}", id),
                        null,
                        (res) => {
                            list = Utils.removeAt(list, getCommentIndex(+id));
                        },
                        (err) => {
                            Utils.log(err);
                        },
                        deleteComment
                    );
                }
            }
        );
    }

    function updateValueDisplayed(value) {
        switch (true) {
            case value > 0:
                valueDisplayed = "Awesome";
                valueIcon = awesomeIcon;
                break;
            case value < 0:
                valueDisplayed = "Not Cool";
                valueIcon = dislikeIcon;
                break;
            default:
                valueDisplayed = "Cool";
                valueIcon = likeIcon;
        } 
    }

    function onCmtValueChange(e) {
        value = e.target.value;

        updateValueDisplayed(value);
        submitRating(value);
    }

    function rateComment(e) {
        let id = e.currentTarget.getAttribute("itemId");
        let modal = document.getElementById("myModal");
        Utils.log('Rate this comment!');

        commentId = id && id.replace('rate_', '');

        Request.get(
            urlConst.get_comment_rating
                .replace("{userId}", SessionUtil.get("info", true).userId)
                .replace("{commentId}", commentId),
            null,
            (res) => {
                value = res['crating'][0].rating;
                updateValueDisplayed(res['crating'][0].rating);
                Utils.log(res);
            },
            (err) => {
                Utils.log(err);
            },
            submitRating
        );

        modal.style.display = "flex";
    }

    function closeModal(){
        let modal = document.getElementById("myModal");

        modal.style.display = "none"; 
    }

    function submitRating(value) {
        Request.post(
            urlConst.post_comment_rating
                .replace("{userId}", SessionUtil.get("info", true).userId)
                .replace("{commentsId}", commentId),
            {
                rating: value
            },
            (res) => {
                Utils.log(res);
            },
            (err) => {
                Utils.log(err);
            },
            submitRating
        );
    }

    function handleResize() {
        let adjustHeight = (document.body.classList.contains('x-mobile')) ? 120 : 70;

        if(cmpEl) {
            cmpEl.style.height = Utils.calculateAvailableSpace(cmpEl, adjustHeight);
        }

        setTimeout(() => {
            if(cmpEl){
                let adjustHeight = (document.body.classList.contains('x-mobile')) ? 120 : 70;

                cmpEl.style.height = Utils.calculateAvailableSpace(cmpEl, adjustHeight);
            }
        }, 1000);
    }

</script>

<svelte:window on:orientationchange={handleResize} />

<div class="comments-container">
    <div class="comments overflow-y" bind:this={cmpEl}>
        <InfiniteLoading
            on:infinite={infiniteHandler}
            {noResultsText}
        />

        {#each list as item, index}
            <div class="comments-item" data-num={list.length - index}>
                <div class="question-text">{item.content}</div>
                {#if postUserId === userId}
                    <span
                         class="material-icons collab-btn"
                         on:click={collaborate}
                         itemId={"collab_" + item.id}>people-plus</span
                     >
                {/if}
                {#if (item.user && item.user.userId) === userId}
                     <span
                        class="material-icons delete-btn"
                        on:click={deleteComment}
                        itemId={item.id}>delete</span
                    >
                {/if}
                {#if (item.user && item.user.userId) !== userId}
                <span
                    class="material-icons rate-btn"
                    on:click={rateComment}
                    itemId={"rate_" + item.id}>star_rate</span
                >
                {/if}
                <div class="feed-info qtn-auth-cont">
                    <div class="qtn-mdle-auth-details txt-right">
                        <span class="qtn-mdle-auth-name"
                            >{item.user && item.user.firstName}
                            {item.user && item.user.lastName}</span
                        >
                        <span class="published-details"
                            >{Labels.profile.published}: {TIME_SINCE(
                                list[index].addedDate
                            )}</span
                        >
                    </div>
                </div>
            </div>
        {/each}
    </div>
    <Toolbar>
        <TextField
            slot="center"
            cls="comments-field"
            placeholder= {Labels.placeholder.comments}
            maxLength={maxLength}
            bind:value={commentValue}
            on:enter={onSend}
        />
        <Button
            iconCls="material-icons"
            iconText="send"
            slot="right"
            cls="comments-btn"
            on:click={onSend}
        />
    </Toolbar>
</div>

<!-- The Modal -->
<div id="myModal" class="modal">

    <!-- Modal content -->
    <div class="modal-content">
        <div align="right">
            <span class="close" on:click={closeModal}>&times;</span>
        </div>
        <div align="center" class="rating-container wh-100-percent">
            <label for="points">Your rating for the selected Comment: 
                <b>{valueDisplayed} 
                    <img class="icon-cont" alt="" width="13px" src={valueIcon} />
                </b>
            </label>
            <input
                type="range"
                min="-5"
                max="5"
                {value}
                class="slider"
                on:change={onCmtValueChange}
            />
            <div class="slider-values">
                <span>Not Cool <img class="icon-cont" alt="" width="13px" src={dislikeIcon} /></span>
                <span>Cool <img class="icon-cont" alt="" width="13px" src={likeIcon} /></span>
                <span>Awesome <img class="icon-cont" alt="" width="13px" src={awesomeIcon} /></span>
            </div>
        </div>
    </div>
  
</div>

<style>
    :global(.comments-container .field-container){
        width: 85%;
    }
</style>
