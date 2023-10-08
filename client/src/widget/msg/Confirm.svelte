<script>
    import Toolbar from "../toolbar/Toolbar.svelte";
    import Button from "../button/Button.svelte";
    import Utils from "../../util/Utils";

    export let title = "Alert";
    export let message = "";
    export let confirmText = "Ok";
    export let cancelText = "Cancel";
    export let callback = (id) => {};

    function doCallback(action) {
        Utils.hideAlert();
        callback(action);
    }

    function onOkClick(e) {
        doCallback("ok");
    }

    function onCancelClick() {
        doCallback("cancel");
    }
</script>

<div class="msg wh-100-percent">
    <div class="msg-mask wh-100-percent" />
    <div class="flex-cont flex-vh msg-body flex-dir-column">
        <div class="msg-body-inner border-radius-10">
            <div class="text-center p-2 border-radius-10">
                <div class="title pl-1 mx-auto text-dark font-weight-bold">{title}</div>
            </div>
            <div class="msg-body-text flex-cont flex-vh">
                {message}
            </div>
            <Toolbar ui="plain" cls="border-radius-10">
                <div slot="right" class="flex-cont rounded">
                    <Button
                        text={cancelText}
                        slot="right"
                        cls="border-radius-10 col-10 mx-auto"
                        ui="cancel"
                        on:click={onCancelClick}
                    />
                    <Button
                        text={confirmText}
                        slot="right"
                        cls="border-radius-10 col-10 mx-auto"
                        on:click={onOkClick}
                    />
                </div>
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
</style>
