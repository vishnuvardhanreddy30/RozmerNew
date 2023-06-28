<script>
    export let cls = "";
    export let ui = "container";
</script>

<div class="toolbar flex-cont flex-vh {cls} toolbar-ui-{ui}">
    <div class="left-cont">
        <slot name="left" />
    </div>

    <div class="center-cont flex-1 flex-cont flex-vh">
        <slot name="center" />
    </div>

    <div class="right-cont">
        <slot name="right" />
    </div>
</div>

<style>
    .toolbar {
        padding: 0.3rem;
        min-height: 2.3rem;
    }
</style>
