<script>
    import { onMount } from "svelte";
    import SessionUtil from "../../util/SessionUtil";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    import Utils from "../../util/Utils";

    let loginUserInfo = {};
    let data = [];
    let containerEl;
    let availableHeight;

    function fetchUserComments(uid) {
        if (!uid) return;
        const url = urlConst.get_user_comments.replace("{userId}", uid);
        Utils.mask(true);
        Request.get(url, null, onSuccess, onFailure, onSuccess);
    }

    function onSuccess(res = []) {
        data = res.filter(item => item.hidePost !== '1').reverse();
        Utils.mask();
    }

    function onFailure(err) {
        Utils.log(err);
        Utils.mask();
    }

    function navigateToPost(postId) {
        Utils.redirectTo("articles", { pid: postId });
    }

    onMount(() => {
        loginUserInfo = SessionUtil.get("info", true);
        if (loginUserInfo?.userId) {
            fetchUserComments(loginUserInfo.userId);
        }
        availableHeight = Utils.calculateAvailableSpace(containerEl);
    });
</script>

<div
    class="comment-list-container overflow-y"
    style="height:{availableHeight}"
    bind:this={containerEl}
>
    <div class="comment-items">
        {#each data as item}
            <div class="comment-card">
                <i class="fa-regular fa-comment-dots comment-icon"></i>
                <div class="comment-content">{item.content}</div>
                <i
                    class="fa-solid fa-arrow-right navigate-icon" title="Open post"
                    on:click={() => navigateToPost(item.postId)}
                ></i>
            </div>
        {/each}

        {#if Utils.isEmpty(data)}
            <div class="no-comments-msg">No comments found</div>
        {/if}
    </div>
</div>

<style>
    @import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css');

    .comment-list-container {
        padding: 0.5rem;
    }

    .comment-items {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
    }

    .comment-card {
        display: flex;
        align-items: center;
        background: #f8f9fa;
        border-radius: 10px;
        padding: 0.75rem 1rem;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
    }

    .comment-icon {
        margin-right: 0.75rem;
        color: #6c757d;
        font-size: 1.2rem;
    }

    .comment-content {
        flex: 1;
        font-size: 0.95rem;
        color: #333;
        word-break: break-word;
    }

    .navigate-icon {
        color: #007bff;
        font-size: 1.1rem;
        cursor: pointer;
        transition: transform 0.2s ease;
    }

    .navigate-icon:hover {
        transform: scale(1.2);
    }

    .no-comments-msg {
        text-align: center;
        padding: 2rem;
        color: #888;
        font-style: italic;
    }
</style>
