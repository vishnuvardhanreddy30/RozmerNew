<svelte:options accessors />

<script>
    import { onMount, beforeUpdate } from "svelte";
    import VirtualList from "../../widget/tinylist/VirtualList.svelte";
    import InfiniteLoading from "../../widget/tinylist/InfiniteLoading.svelte";
    import FeedDetails from "./FeedDetails.svelte";
    import FeedDetailsMobile from "./FeedDetailsMobile.svelte";
    import Utils from "../../util/Utils";
    import urlConst from "../../const/Url";
    import TIME_SINCE from "../../util/Time";
    import proIcon from "../../assets/profile-list-icon.png";
    import no_image from "../../assets/no_image.png";
    import Labels from "../../const/Labels";
    import Request from "../../util/Request";
    import Boot from "../../util/Boot";
    import Share from "../../widget/Share.svelte";
    import SessionUtil from "../../util/SessionUtil";

    let api = urlConst.get_all_post,
        itemSize = 130, // list item height
        page = 1,
        listHeight = 0,
        list = [],
        showDetails = false,
        detail,
        postId,
        infiniteId = Symbol(),
        category = Utils.getHash() == 'articles' ? 'article' : Utils.getHash() == 'poems' ? 'poem' : '';
    let initialLoad = true
    let userId = SessionUtil.get("info", true).userId;
    let modalMessage = "You've alreday viewed this post (article/poem). If you want to watch it again please clear the due amount for this post";
    
    function infiniteHandler({ detail: { loaded, complete } }) {
        Request.get(
            `${api}?pageNumber=${page - 1}&pageSize=10&sortDir=desc&category=${category}`,
            null,
            (data) => {
                initialLoad = false;
                let records = data.content;
                // Fetch payment details from localStorage
                let paymentDetails = JSON.parse(localStorage.getItem("paymentDetails") || "[]");

                if (!Utils.isEmpty(records)) {
                    page += 1;
                    let newRecords = [];

                    for(let i = 0; i < records.length; i++) {
                        if(records[i].hidePost !== '1') {
                            // Check if the postId matches with paymentDetails
                            const isViewed = paymentDetails.some(
                                (payment) => payment.postId === records[i].postId
                            );
                            // Add a viewed property to the record
                            records[i].viewed = isViewed;
                            newRecords.push(records[i]);
                        } 
                    }

                    list = [...list, ...newRecords];

                    if (list.length >= data.totalRecords) {
                        complete();
                    } else {
                        loaded();
                    }
                } else {
                    complete();
                }
            },
            () => {
                Utils.log("Failed to load feed data");
            },
            onHideDetails
        );
    }

    export function onItemClick(e, routeData) {
        detail = null;
        showDetails = false;

        let idx = e && e.currentTarget.getAttribute("data-num");
        detail = list[+idx - 1] || {};

        // Check if the logged-in user is the post's author
        if (detail?.user?.userId === userId) {
            postId = detail.postId;
            showDetails = true;

            if (getPID() !== String(postId)) {
                Utils.redirectTo(Utils.getHash(), {
                    pid: postId,
                });
            }
            return; // Exit here to skip further checks
        }

        // If not the author, check the viewed status
        if (detail.viewed) {
            // Logic to open the payment modal goes here
            openModal();
        } else {
            // If accessed via routeData, update the post ID
            if (routeData) {
                idx = routeData.params.id;
                detail = {
                    postId: idx,
                };
            }

            // Mark the post as viewed
            detail.viewed = true;

            // If the post has a title, record the view in paymentDetails
            if (detail.title) {
                let paymentDetails = JSON.parse(localStorage.getItem("paymentDetails") || "[]"); // Parse or use an empty array
                let obj = {
                    title: `Read ${detail.category}: ${detail.title}`,
                    postId: detail.postId,
                    type: 'Post Read',
                    date: new Date(),
                    amount: '10',
                };
                paymentDetails.push(obj); // Add the new payment detail
                localStorage.setItem("paymentDetails", JSON.stringify(paymentDetails)); // Save back to localStorage
            }
            // Navigate to post details
            postId = detail.postId;
            showDetails = true;

            if (getPID() !== String(postId)) {
                Utils.redirectTo(Utils.getHash(), {
                    pid: postId,
                });
            }
        }
    }


    function onHideDetails() {
        detail = null;
        showDetails = false;
    }

    export function onSearch(text) {
        text = String(text).trim();

        api = Utils.isEmpty(text)
            ? urlConst.get_all_post
            : urlConst.get_post_by_title.replace("{keywords}", text);

        page = 1;
        list = [];
        infiniteId = Symbol();
    }

    export function onRouteChange(data) {
        if (data.params) {
            if (Utils.isEmpty(data.params.pid)) {
                onHideDetails();
            }

            if (!Utils.isEmpty(data.params.pid)) {
                showDetailsFromRoute(data.params.pid);
            }
        }
        initialLoad = true;

        // Reset state and trigger infinite scroll for initial data fetch
        category = Utils.getHash() == 'articles' ? 'article' : Utils.getHash() == 'poems' ? 'poem' : '';
        page = 1;
        list = [];
        infiniteId = Symbol();
        // Ensure the first fetch occurs only once by directly calling the handler
setTimeout(() => {
        if (list.length === 0) {

            infiniteHandler({ detail: { loaded: () => {}, complete: () => {} } });
        }
    }, 0);
    }

    function hideDetailsPopup() {
        showDetails = false;
    }

    function onFabClick() {
        Utils.redirectTo('publish');
    }

    function showDetailsFromRoute(pid) {
        onItemClick(null, {
            params: {
                id: pid
            }
        });
    }

    function getPID() {
        return Utils.getParamsAsObject(location.hash).pid;
    }

    onMount(() => {
        let pid = getPID();

        if(pid) {
            showDetailsFromRoute(pid);
        }
// Ensure the first fetch occurs only once by directly calling the handler
setTimeout(() => {
        if (list.length === 0) {

            infiniteHandler({ detail: { loaded: () => {}, complete: () => {} } });
        }
    }, 0);    });

    beforeUpdate(() => {
        if (category !== (Utils.getHash() == 'articles' ? 'article' : Utils.getHash() == 'poems' ? 'poem' : '')) {
            onRouteChange({});
        }
    });

    let isModalOpen = false;

    function openModal() {
        isModalOpen = true;
    }

    function closeModal() {
        isModalOpen = false;
    }
    function navigateToPayments() {
        closeModal();
        Utils.redirectTo('payment'); // Replace 'payments' with your actual payments page route
    }
</script>

<div class="feed-list flex-cont">
    <div class="list flex-cont flex-vh flex-1" bind:offsetHeight={listHeight}>
        <VirtualList height={listHeight} {itemSize} itemCount={list.length}>
            <div
                slot="item"
                let:index
                let:style
                {style}
                class="virtual-list-item-body"
                data-num={index + 1}
                on:click={onItemClick}
            >
                <div class="virtual-list-item">
                    <div class="w-90-percent">
                    <div class="feed-info flex-cont">
                        <!-- <figure>
                            <img
                                src={proIcon}
                                width="36px"
                                height="36px"
                                alt=""
                            />
                        </figure> -->
                        <div class="bg-img profile-image pointer user-profile-image" style="background-image: url({list[index].user.imageName ? urlConst.get_profile_pic +list[index].user.imageName : proIcon});"/>
                        <div class="author-details flex-cont space-between">
                            <div>
                            <span class="author-name"
                                >Written by {list[index].user &&
                                    list[index].user.firstName}
                                {list[index].user &&
                                    list[index].user.lastName}</span
                            >
                            <span class="published-details ml-2"
                                > | {Labels.profile.published} {TIME_SINCE(
                                    list[index].addedDate
                                )}</span
                            >
                        </div>
                        </div>
                    </div>
                    <div class="thumb-det-cont">
                        <div class="thumb-title">
                            <b>{list[index].title}</b>
                        </div>
                    </div>
                    </div>
                    <div
                        class="bg-img feed-thumbnail"
                        style="background-image: url({list[index].imageName
                            ? urlConst.get_thumbnail_image +
                              list[index].imageName
                            : no_image});"
                    />
                </div>
            </div>

            <div slot="footer" class="footer">
                {#if !initialLoad}
                <InfiniteLoading
                    on:infinite={infiniteHandler}
                    identifier={infiniteId}
                    noResultsText=""
                />
                {/if}
            </div>
        </VirtualList>
    </div>
</div>

<!-- {#if Boot.isDesktop()}
    <div class="fab-btn flex-cont flex-vh tooltip" data-qtip="{Labels.tip.publish_fab}" on:click={onFabClick}>
        <span class="material-icons">add</span>
    </div>
{/if} -->
{#if showDetails}
    {#if Boot.isDesktop()}
        <FeedDetails {postId} on:hidedetails={onHideDetails} on:hidedetailpopup={hideDetailsPopup}/>
    {:else}
        <FeedDetailsMobile {postId} on:hidedetails={onHideDetails} on:hidedetailpopup={hideDetailsPopup}/>
    {/if}
{/if}
{#if isModalOpen}
<div class="payment-modal-backdrop">
    <div class="payment-modal">
        <p>{modalMessage}</p>
        <button on:click={navigateToPayments}>Payment</button>
        <button on:click={closeModal}>Close</button>
    </div>
</div>
{/if}

<style>
    .list {
        flex-grow: 1;
    }
    .list :global(.virtual-list-wrapper) {
        /* overflow: visible; */
        white-space: nowrap;
        /* max-width: 600px; */
        overflow-x: hidden;
    }
    .footer{
        padding-bottom: 2%;
    }
    .virtual-list-item-body {
        /* max-width: 96%; */
        cursor: pointer;
        /* max-width: 800px; */
        /* left: auto !important; */
        /* padding-left: 1rem; */
        /* margin-top: 1rem; */
    }

    .fab-btn {
        position: absolute;
        bottom: 1rem;
        right: 2rem;
        border-radius: 50%;
        width: 60px;
        height: 60px;
        background: var(--primary-color-alternate-2);
        box-shadow: 0 1px 4px rgb(0 0 0 / 60%);
        color: var(--white-color);
        cursor: pointer;
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
</style>
