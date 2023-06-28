<script>
    import Toolbar from "../toolbar/Toolbar.svelte";
    import Button from "../button/Button.svelte";
    import Utils from "../../util/Utils";
    import urlConst from "../../const/Url";

    export let title = "Preview";
    export let file = null;
    export let url = null; // TODO - implement for URL

    let image;

    $: {
        if (file && !url) {
            const reader = new FileReader();

            reader.addEventListener("load", function () {
                image.setAttribute("src", reader.result);
            });

            reader.readAsDataURL(file);
        }
    }

    function onClose() {
        Utils.hideAlert();
    }
</script>

<div class="msg wh-100-percent">
    <div class="msg-mask wh-100-percent" />
    <div class="flex-cont flex-vh msg-body flex-dir-column">
        <div class="msg-body-inner">
            <Toolbar cls="theme-bg">
                <div class="title pl-1" slot="left">{title}</div>
                <Button
                    iconCls="material-icons close-icon"
                    iconText="close"
                    ui="cancel"
                    on:click={onClose}
                    slot="right"
                />
            </Toolbar>
            <div class="msg-body-text flex-cont flex-vh">
                {#if file}
                    <img
                        bind:this={image}
                        src=""
                        alt="Preview"
                        class="preview-img"
                    />
                {/if}
                {#if url}
                    <img
                        bind:this={image}
                        src={urlConst.get_thumbnail_image + url}
                        class="preview-img"
                        alt="Preview"
                    />
                {/if}
            </div>
            <div />
        </div>
    </div>
</div>

<style>
    /* TODO- Common styling has to be moved  */
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

    .preview-img {
        max-width: 600px;
        width: 100%;
    }

    :global(.close-icon) {
        color: var(--close-icon-color);
    }
</style>
