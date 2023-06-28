<script>
    import VirtualList from "svelte-tiny-virtual-list";
    import InfiniteLoading from "svelte-infinite-loading";

    export let api;
    export let itemSize = 300; // list item height
    export let tplCallback = () => {};

    let page = 1;
    let listHeight = 0;
    let list = [];

    function infiniteHandler({ detail: { loaded, complete } }) {
        fetch(`${api}&page=${page}`)
            .then((response) => response.json())
            .then((data) => {
                if (data.hits.length) {
                    page += 1;
                    list = [...list, ...data.hits];
                    loaded();
                } else {
                    complete();
                }
            });
    }
</script>

<div class="test">
    <div class="list" bind:offsetHeight={listHeight}>
        <VirtualList height={listHeight} {itemSize} itemCount={list.length}>
            <div
                slot="item"
                let:index
                let:style
                {style}
                class="virtual-list-item"
                data-num={index + 1}
            >
                <!-- <a target="_blank" href={list[index].url}>{list[index].title}</a
                >
                <p>
                    <span>{list[index].points}</span>
                    points by
                    <a
                        target="_blank"
                        href="https://news.ycombinator.com/user?id={list[index]
                            .author}">{list[index].author}</a
                    >
                    |
                    <a
                        target="_blank"
                        href="https://news.ycombinator.com/item?id={list[index]
                            .objectID}">{list[index].num_comments} comments</a
                    >
                </p> -->
            </div>

            <div slot="footer">
                <InfiniteLoading on:infinite={infiniteHandler} />
            </div>
        </VirtualList>
    </div>
</div>

<style>
    .virtual-list-item {
        margin-bottom: 0.3rem;
    }
    .test {
        height: 100%;
        width: 100%;
        display: flex;
    }
    .list {
        flex-grow: 1;
    }
    .list :global(.virtual-list-wrapper) {
        overflow: visible;
        overflow-x: hidden;
        white-space: nowrap;
    }
</style>
