<script context="module">
    let id = -1;
</script>

<script>
    import { getContext, setContext, onMount } from "svelte";
    import { key, tabContext } from "./TabManager";
    import SelectField from "../fields/SelectField.svelte";

    let cmpId = "tabPanel-" + ++id;
    let headerCmp;
    let bodyCmp;

    export let items;

    export let callback = function () {};

    export let viewData = {};

    export let cls = "";

    setContext(key, tabContext);

    const context = getContext(key);

    let obj = context.initialize(items, cmpId, callback, viewData);

    headerCmp = obj.header;
    bodyCmp = obj.body;

    onMount(() => {
        context.activateTab({
            parentId: cmpId,
        });
    });
</script>

<div class="tab-panel {cls}">
    <div class="tab-header">
        <svelte:component this={headerCmp} />
    </div>

    <div class="tab-panel-container overflow">
        <svelte:component
            this={bodyCmp}
            bind:this={context.components[cmpId].bodyRef}
        />
    </div>
</div>
