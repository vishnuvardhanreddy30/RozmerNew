<script>
    import { getContext } from "svelte";
    import Tab from "./Tab.svelte";
    import { key } from "./TabManager";

    export let items = [];
    export let parentId;
    // export let tabId;

    const context = getContext(key);

    function onTabClick(e) {
        context.activateTab(e.detail);
    }
</script>

<div class="tab-header-container">
    {#each items as item}
        <Tab
            {...item}
            bind:this={context.components[parentId].tabs[item.id]}
            on:tabclick={onTabClick}
        />
    {/each}
</div>

<style>
    .tab-header-container {
        border-bottom: 1px solid teal;
        overflow-y: hidden;
        display: flex;
        overflow-x: auto;
    }
</style>
