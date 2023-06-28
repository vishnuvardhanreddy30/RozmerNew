<script>
    import { onMount } from "svelte";

    import InfiniteLoading from "../../widget/tinylist/InfiniteLoading.svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import Utils from "../../util/Utils";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Base from "../../util/Base";
    import Labels from "../../const/Labels";
    import SessionUtil from "../../util/SessionUtil";
    // import proIcon from "../../assets/profile-list-icon.png";
    import TIME_SINCE from "../../util/Time";
    import likeIcon from "../../assets/rating/like.png";
    import dislikeIcon from "../../assets/rating/dislike.png";
    import awesomeIcon from "../../assets/rating/ok.png";

    export let postId = "";
    export let details;

    export let postUserId = null;

       
    let value = 0;
    let valueDisplayed = "Cool";
    let valueIcon = likeIcon;
    let maxLength = 999;

    let cmpEl, questionValue, pressedReplyBtn, questionId, noResultsText;

    let userId = SessionUtil.get("info", true).userId;
    onMount(() => {
        handleResize();

        noResultsText = (postUserId !== userId) ? Labels.list.question_no_results : "";
    });

    const api = urlConst.get_post_questions.replace("{postId}", postId);

    let page = 0;
    let list = [];

    function infiniteHandler({ detail: { loaded, complete } }) {
        // reset the button reply button from send to reply
        if (pressedReplyBtn) {
            pressedReplyBtn.textContent = "reply";
            Base.createReplyField();
            pressedReplyBtn = null;
        }

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
                        : (postUserId !== userId) ? "" : Labels.list.question_no_results;
                } else {
                    complete();
                }
            },

            (err) => {
                Utils.log("[Questions] Failed to load Questions");
            },

            infiniteHandler
        );
    }

    function onSend() {
        if (Utils.isEmpty(questionValue)) {
            return;
        }

        Request.post(
            urlConst.post_question
                .replace("{postId}", postId)
                .replace("{userId}", userId),
            {
                questions: questionValue,
            },
            (res) => {
                Base.toast("success", Labels.question.success, 3000);
                res.answer = res.answer || [];
                list = [...list, ...[res]];
                Utils.log(res);

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
        questionValue = "";
    }

    function getRecord(recId) {
        let record;

        for (let i = 0; i < list.length; i++) {
            if (list[i].questionId === recId) {
                record = list[i];
                break;
            }
        }

        return record;
    }
    function postReply(e) {
        let btnEl = e.currentTarget,
            isSend = false;

        if (pressedReplyBtn) {
            if (
                pressedReplyBtn.textContent === "send" &&
                !!Base.replyField.value
            ) {
                isSend = true;
            }
            
            if(!btnEl) {
                btnEl = pressedReplyBtn.parentElement;
            }

            pressedReplyBtn.textContent = "reply";
            btnEl.classList.add('show-reply-text');
        }

        if (!isSend) {
            let btnWrapper = btnEl.parentElement.parentElement;
            let inputHolder = btnWrapper.previousElementSibling;

            Base.createReplyField(inputHolder, {
                autofocus: true, 
                placeholder: Labels.placeholder.answer,
                maxLength: maxLength
            });

            Base.replyField.$on('enter', postReply);
            btnEl.firstChild.textContent = "send";
            btnEl.classList.remove('show-reply-text');
            pressedReplyBtn = btnEl.firstChild;
        } else {
            // to make sure no network calls made when field is empty
            if (!Base.replyField.value) {
                return;
            }

            let recId = btnEl.getAttribute("rec-id");
            let record = getRecord(+recId);

            Request.post(
                urlConst.post_answer
                    .replace("{questionId}", recId)
                    .replace("{userId}", userId),
                {
                    answer: Base.replyField.value,
                },
                (res) => {
                    Base.createReplyField();
                    Base.toast("success", "Reply posted");
                    record.answer = [...record.answer, ...[res]];
                    list = [...list];
                },
                (err) => {
                    Base.toast("warning", "Failed to post reply");
                },
                postReply
            );

            pressedReplyBtn = null;
        }
    }

    function getQtnInx(id) {
        for (let k = 0; k < list.length; k++) {
            if (list[k].questionId === id) {
                return k;
            }
        }
    }

    function getAnswerIndex(id, qtnid) {
        let ansArr = list[qtnid].answer;

        for (let j = 0; j < ansArr.length; j++) {
            if (ansArr[j].answerId === id) {
                return j;
            }
        }
    }

    function getQuestionIndex(id) {
        for (let i = 0; i < list.length; i++) {
            if (list[i].questionId === id) {
                return i;
            }
        }
    }

    function deleteQuestion(e) {
        let id = e.currentTarget.getAttribute("itemId");
        
        Utils.confirm(
            Labels.question.question_delete_confirm,
            Labels.question.question_delete_title,
            function (btn) {
                if (btn === "ok") {
                    Request.delete(
                        urlConst.delete_question.replace("{questionId}", id),
                        null,
                        (res) => {
                            list = Utils.removeAt(list, getQuestionIndex(+id));
                        },
                        (err) => {
                            Utils.log(err);
                        },
                        deleteQuestion
                    );
                }
            }
        );
    }

    function deleteAns(e) {
        let answerId = e.currentTarget.getAttribute("itemId");
        let questionId = e.currentTarget.getAttribute("id");
        let questionIdx = getQtnInx(+questionId);

        Utils.confirm(
            Labels.question.answer_delete_confirm,
            Labels.question.answer_delete_title,
            function (btn) {
                if (btn === "ok") {
                    Request.delete(
                        urlConst.delete_answers.replace("{answerId}", answerId),
                        null,
                        (res) => {
                            list[questionIdx].answer = Utils.removeAt(list[questionIdx].answer, getAnswerIndex(+answerId, +questionIdx));
                        },
                        (err) => {
                            Utils.log(err);
                        },
                        deleteAns
                    );
                }
            }
        );
    }

    function collaborate(e) {
        let id = e.currentTarget.getAttribute("itemId");
        Utils.confirm(
            Labels.question.collaborate_confirm_msg,
            Labels.question.collaborate_title,
            function (btn) {
                if (btn === "ok") {
                    Base.toast('info', Labels.collaborate.collaborate_body, 3000);

                    let commentData = list[getQuestionIndex(+id.slice(7))] || {};

                    window['collaborateDetails'] = {
                        postId: postId,
                        details: details,
                        questionId: commentData.questionId
                    }
                    Utils.redirectTo('collaborate');
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

    function onQtnValueChange(e) {
        value = e.target.value;

        updateValueDisplayed(value);
        submitRating(value);
    }

    function rateQuestion(e) {
        let id = e.currentTarget.getAttribute("itemId");
        let modal = document.getElementById("myModal");
        Utils.log('Rate this comment!');

        questionId = id && id.replace('rate_', '');

        Request.get(
            urlConst.get_question_rating
                .replace("{userId}", SessionUtil.get("info", true).userId)
                .replace("{qnId}", questionId),
            null,
            (res) => {
                value = res['qrating'][0].rating;
                updateValueDisplayed(res['qrating'][0].rating);
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
            urlConst.post_question_rating
                .replace("{userId}", SessionUtil.get("info", true).userId)
                .replace("{questionId}", questionId),
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
<div class="questions-container">
    <div class="questions overflow-y" bind:this={cmpEl}>
        <InfiniteLoading
            on:infinite={infiniteHandler}
            {noResultsText}
        />

        {#each list as item, index}
            <div class="question-item" data-num={list.length - index}>
                <div class="question-item-q">
                    <div class="question-text">{item.questions}</div>
                    {#if (item.user && item.user.userId) === userId}
                        <span
                            class="material-icons delete-btn"
                            on:click={deleteQuestion}
                            itemId={item.questionId}>delete</span
                        >
                    {/if}
                    {#if postUserId === userId}
                        <span
                            class="material-icons collab-btn"
                            on:click={collaborate}
                            itemId={"collab_" + item.questionId}>people-plus</span
                        >
                    {/if}
                    {#if (item.user && item.user.userId) !== userId}
                        <span
                            class="material-icons rate-btn"
                            on:click={rateQuestion}
                            itemId={"rate_" + item.questionId}>star_rate</span
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
                <div />
                <div class="reply-cont">
                    {#each item.answer as reply}
                        <div class="reply-item">
                            <div class="reply-text">
                                <span class="reply-ans-str">{reply.answer}</span>
                                {#if (reply.user && reply.user.userId) === userId}
                                    <span
                                        class="material-icons ans-dlt-btn"
                                        on:click={deleteAns}
                                        id="{item.questionId}"
                                        itemId={reply.answerId}>delete</span
                                    >
                                {/if}
                            </div>
                            <div class="feed-info qtn-auth-cont">
                                <div class="qtn-mdle-auth-details txt-right">
                                    <span class="qtn-mdle-auth-name">
                                        {reply.user && reply.user.firstName}
                                        {reply.user && reply.user.lastName}
                                    </span>
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
                    <div slot="center" />
                    <Button
                        iconCls="material-icons"
                        iconText="reply"
                        ui="action"
                        slot="right"
                        cls="question-reply-btn show-reply-text"
                        on:click={postReply}
                        itemId={item.questionId}
                    />
                </Toolbar>
            </div>
        {/each}
    </div>
    {#if postUserId !== userId}
        <Toolbar>
            <TextField
                slot="center"
                cls="comments-field"
                placeholder=  {Labels.placeholder.question}
                maxLength={maxLength}
                bind:value={questionValue}
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
    {/if}
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
                on:change={onQtnValueChange}
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
    :global(.questions-container .field-container){
        width: 85%;
    }
    .reply-cont .reply-item {
        padding: 6px 0 6px 1rem;
        margin: 16px 0 0 1rem;
        background-color: var(--body-bg-color);
    }

    :global(.question-reply-btn.btn-el) {
        padding: 0px;
        top: 10px;
    }

    :global(.show-reply-text::before) {
        content: "Answer";
        margin-right: 6px;
    }

    :global(.reply-ans-str) {
        width: 90%;
        display: inline-block;
    }
</style>
