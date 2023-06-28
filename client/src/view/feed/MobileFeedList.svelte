<!-- Not using this file -->
<script>
    import VirtualList from "../../widget/tinylist/VirtualList.svelte";
    import InfiniteLoading from "../../widget/tinylist/InfiniteLoading.svelte";
    import FeedDetailsMobile from "./FeedDetailsMobile.svelte";
    import Utils from "../../util/Utils";
    import urlConst from "../../const/Url";
    import TIME_SINCE from "../../util/Time";
    import proIcon from "../../assets/profile-list-icon.png";
    import no_image from "../../assets/no_image.png";
    import Labels from "../../const/Labels";
    import Request from "../../util/Request";

    let api = urlConst.get_all_post; //"https://hn.algolia.com/api/v1/search_by_date?tags=story";
    let itemSize = 350; // list item height

    let page = 1;
    let listHeight = 0;
    let list = [];

    let showDetails = false;
    let detail, postId;

    let infiniteId = Symbol();

    function infiniteHandler({ detail: { loaded, complete } }) {
        Request.get(
            `${api}?pageNumber=${page - 1}&pageSize=10&sortDir=desc`,
            null,
            (data) => {
                let records = data.content;

                if (!Utils.isEmpty(records)) {
                    page += 1;
                    let newRecords = [];

                    for(let i = 0; i < records.length; i++) {
                        if(records[i].hidePost !== '1') {
                            newRecords.push(records[i]);
                        } 
                    }

                    list = [...list, ...newRecords];

                    if (data.totalRecords === records.length) {
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

        var idx = e && e.currentTarget.getAttribute("data-num");

        detail = list[+idx - 1] || {};
        if (routeData) {
            idx = routeData.params.id;

            detail = {
                postId: idx,
            };
        }

        postId = detail.postId;
        showDetails = true;

        // Utils.redirectTo("details", {
        //     postId: postId,
        // });
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
                    <div class="thumb-det-cont">
                        <div class="thumb-title">
                            <b>{list[index].title}</b>
                        </div>
                    </div>
                    <div
                        class="bg-img feed-thumbnail"
                        style="background-image: url({list[index].imageName
                            ? urlConst.get_thumbnail_image +
                              list[index].imageName
                            : no_image});"
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
                                >{list[index].user &&
                                    list[index].user.firstName}
                                {list[index].user &&
                                    list[index].user.lastName}</span
                            >
                            <span class="published-details"
                                >{Labels.profile.published}: {TIME_SINCE(
                                    list[index].addedDate
                                )}</span
                            >
                        </div>
                    </div>
                </div>
            </div>

            <div slot="footer">
                <InfiniteLoading
                    on:infinite={infiniteHandler}
                    noResultsText={Labels.list.no_results}
                    identifier={infiniteId}
                />
            </div>
        </VirtualList>
    </div>
</div>

{#if showDetails}
    <FeedDetailsMobile {postId} on:hidedetails={onHideDetails} />
{/if}
