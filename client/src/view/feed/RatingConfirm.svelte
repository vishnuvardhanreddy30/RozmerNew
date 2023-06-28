<script>
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import Labels from "../../const/Labels";
    import Utils from "../../util/Utils";

    export let title = Labels.alert.confirm;
    export let message = Labels.details.rating_confirm_msg;
    export let confirmText = Labels.details.take_down_post;
    export let cancelText = Labels.details.submit_rating_popup;
    export let callback = (id, comment) => {};

    let comment = '',
        commentFld;

    function doCallback(action) {
        Utils.hideAlert();
        callback(action, comment);
    }

    function onOkClick(e) {
        if(Utils.isEmpty(String(comment).trim())) {
            commentFld.focus();
            return
        }

        doCallback(1);
    }

    function onCancelClick() {
        doCallback(0);
    }
</script>

<div class="msg wh-100-percent">
    <div class="msg-mask wh-100-percent" />
    <div class="flex-cont flex-vh msg-body flex-dir-column">
        <div class="msg-body-inner">
            <Toolbar cls="theme-bg">
                <div class="title pl-1" slot="left">{title}</div>
                <!-- <Button text={confirmText} slot="right" /> -->
            </Toolbar>
            <div class="msg-body-text flex-cont flex-dir-column">
                <p class="flex-cont flex-dir-column">
                {@html message}
                </p>
                <TextField label="Please type here" bind:value={comment} bind:this={commentFld}></TextField>
            </div>
            <Toolbar ui="plain">
                <div slot="right" class="flex-cont">
                    <Button
                        text={cancelText}
                        ui="cancel"
                        on:click={onCancelClick}
                    />
                    <Button
                        text={confirmText}
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
