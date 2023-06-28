<svelte:options accessors />

<script>
    import { createEventDispatcher } from "svelte";

    export let id;
    export let text;
    export let separator;
    export let disabled = "";
    export let data;

    const dispatch = createEventDispatcher();

    function onClick() {
        dispatch("itemclick", {
            data: data,
            id: id,
        });
    }
</script>

<button class="breadcrumb-btn pc f5" on:click={onClick} class:disabled>
    {text}
</button>
{#if separator}
    <span class="btn-sep b">{separator}</span>
{/if}

<style>
    .breadcrumb-btn {
        height: 48px;
        min-width: 48px;
        border: 0;
        background: transparent;
        cursor: pointer;
    }
</style>
