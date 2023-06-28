<svelte:options accessors />

<script>
    import { createEventDispatcher } from "svelte";

    export let text = "";
    export let parentId;
    export let id;
    export let active;
    export let view;
    export let html;

    const dispatch = createEventDispatcher();

    function onTabClick() {
        dispatch("tabclick", {
            parentId: parentId,
            id: id,
            active: active,
            view: view,
            html: html,
        });
    }
</script>

<button class="tab-btn" on:click={onTabClick} class:active>{text}</button>

<style>
    .tab-btn {
        background: none;
        border: none;
        border-bottom: 2px solid #fff;
        border-radius: 0;
        margin: 0;
        color: #545454;
        height: 48px;
        min-width: 100px;
        font-weight: 600;
        font-size: 14px;
        margin-right: 20px;
    }

    .active {
        border-bottom: 2px solid #00529c;
        color: #00529c;
    }
</style>
