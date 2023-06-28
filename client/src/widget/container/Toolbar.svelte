<script>
</script>

<div class="toolbar-container flex v-center">
    <div class="left">
        <slot name="left" />
    </div>
    <div class="center fl1 flex vh-center">
        <slot name="center" />
    </div>
    <div class="right">
        <slot name="right" />
    </div>
</div>

<style>
    .toolbar-container {
        height: 42px;
        padding: 5px;
    }
</style>
