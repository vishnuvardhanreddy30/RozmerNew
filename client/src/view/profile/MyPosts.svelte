<script>
    import { onMount } from "svelte";
    import FeedDetails from "../feed/FeedDetails.svelte";
    import FeedDetailsMobile from "../feed/FeedDetailsMobile.svelte";
    import SessionUtil from "../../util/SessionUtil";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    import Utils from "../../util/Utils";
    import TIME_SINCE from "../../util/Time";
    import proIcon from "../../assets/profile-list-icon.png";
    import no_image from "../../assets/no_image.png";
    import Boot from "../../util/Boot";
    import Labels from "../../const/Labels";

    let userInfo = {},
        data = [],
        showDetails = false,
        postId,
        showEditPublishBtn,
        availableHeight,
        containerEl;

    function onSuccess(res) {
        res = res || [];

        let filteredRecords = [];

        for(let i = 0; i < res.length; i++) {
            if(res[i].hidePost !== '1') {
                filteredRecords.push(res[i]);
            } 
        }

        data = filteredRecords.reverse();
        Utils.mask();
    }

    function onFailure(err) {
        Utils.log(err);
        Utils.mask();
    }

    function onHideDetails() {
        showDetails = false;
    }

    function showPostDetails(e) {
        postId = e.currentTarget.getAttribute("rec-id");
        showEditPublishBtn = true;
        showDetails = true;
    }

    $: {
        userInfo = SessionUtil.get("info", true);
        let url = urlConst.get_user_posts.replace("{userId}", userInfo.userId);
        Utils.mask(true);
        Request.get(url, null, onSuccess, onFailure, onSuccess);
    }

    onMount(() => {
        availableHeight = Utils.calculateAvailableSpace(containerEl);
    });
</script>

<div
    class="feed-list flex-cont myfeed-cont overflow-y"
    style="height:{availableHeight}"
    bind:this={containerEl}
>
    <div class="flex-1 virtual-list-item-cont">
        {#each data as item}
            <div
                class="flex-1 virtual-list-item-body"
                on:click={showPostDetails}
                rec-id={item.postId}
            >
                <div class="virtual-list-item">
                    <div class="thumb-det-cont">
                        <div class="thumb-title">
                            <b>{item.title}</b>
                        </div>
                    </div>
                    <div
                        class="bg-img feed-thumbnail"
                        style="background-image: url({urlConst.get_thumbnail_image +
                            item.imageName}), url({no_image});"
                    />
                    <div class="feed-info">
                        <figure>
                            <img
                                src={proIcon}
                                width="36px"
                                height="36px"
                                alt=""
                            />
                        </figure>
                        <div class="author-details">
                            <span class="author-name"
                                >{userInfo.firstName}
                                {userInfo.lastName}</span
                            >
                            <span class="published-details"
                                >{Labels.profile.published}: {TIME_SINCE(
                                    item.addedDate
                                )}</span
                            >
                        </div>
                    </div>
                </div>
            </div>
        {/each}

        {#if Utils.isEmpty(data)}
            <div class="no-post-msg-cont flex-dir-column">{@html Labels.profile.no_post}</div>
        {/if}
    </div>
</div>

{#if showDetails}
    {#if Boot.isDesktop()}
        <FeedDetails
            {postId}
            on:hidedetails={onHideDetails}
            {showEditPublishBtn}
        />
    {:else}
        <FeedDetailsMobile
            {postId}
            on:hidedetails={onHideDetails}
            {showEditPublishBtn}
        />
    {/if}
{/if}

<style>
    .virtual-list-item-cont {
        max-width: 100%;
    }
    .myfeed-cont {
        justify-content: center;
    }

    .virtual-list-item-body {
        max-width: 600px;
        margin: 0 auto;
    }

    .feed-thumbnail {
        height: 240px;
        width: 100%;
    }

    .no-post-msg-cont {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        text-align: center;
        line-height: 1.4rem;
        width: 90%;
    }

    @media only screen and (max-width: 600px) {
        /* to avoid display issue for last child in mobile device due to bottom toolbar */
        .virtual-list-item-cont :last-child.virtual-list-item-body {
            padding-bottom: 65px;
        }
    }
</style>
