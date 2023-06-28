<script>
    import Toolbar from "../toolbar/Toolbar.svelte";
    import Button from "../button/Button.svelte";
    import Utils from "../../util/Utils";

    export let title = "Alert";
    export let message = "";
    export let confirmText = "Ok";
    export let callback = () => {};
    export let bodyCls = "";
    export let textBodyCls = "";

    function onOkClick() {
        Utils.hideAlert();

        if (callback) {
            callback.apply();
        }
    }
</script>

<div class="msg wh-100-percent">
    <div class="msg-mask wh-100-percent" />
    <div class="flex-cont flex-vh msg-body flex-dir-column {bodyCls}">
        <div class="msg-body-inner">
            <Toolbar cls="theme-bg">
                <div class="title" slot="left">{title}</div>
                <!-- <Button text={confirmText} slot="right" /> -->
            </Toolbar>
            <div class="msg-body-text flex-cont flex-vh {textBodyCls}">
                {@html message}
            </div>
            <Toolbar ui="plain">
                <Button text={confirmText} slot="center" on:click={onOkClick} />
            </Toolbar>
            <div />
        </div>
    </div>
</div>

<style>
    .msg {
        position: absolute;
        left: 0;
        top: 0;
        background-color: transparent;
        z-index: 99;
    }
    .msg-mask {
        position: absolute;
        left: 0;
        top: 0;
        background: #8f8f8f;
        opacity: 0.6;
        pointer-events: none;
    }

    .msg-body {
        height: 100%;
    }

    .msg-body-text {
        padding: 0.3rem;
        min-height: 3rem;
        line-height: 1.6rem;
    }
</style>
