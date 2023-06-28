<!-- <script>
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Utils from "../../util/Utils";

    function onBack() {
        Utils.redirectBack();
    }
</script>

<div
    class="comments-details"
    transition:fly={{ x: window.innerWidth, duration: 500 }}
>
    <Toolbar cls="theme-bg">
        <div slot="left">
            <Button
                text="Comments"
                iconCls="material-icons"
                iconText="keyboard_backspace"
                on:click={onBack}
            />
        </div>
    </Toolbar>
</div>

<style>
    .comments-details {
        position: absolute;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background-color: #fff;
    }
</style> -->
