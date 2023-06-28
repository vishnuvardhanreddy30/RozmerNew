<svelte:options accessors />

<script>
    export let view;
    export let html;
    export let viewRef;
</script>

<div class="tab-panel-body">
    {#if html}
        {@html html}
    {/if}
    <svelte:component this={view} bind:this={viewRef} />
</div>
