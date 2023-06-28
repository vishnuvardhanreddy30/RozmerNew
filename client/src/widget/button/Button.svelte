<script>
    /**
     * Button Text
     */
    export let text = "";

    /**
     * Extra class
     */
    export let cls = "";

    /**
     * Icon Align
     */
    export let iconAlign = "left";

    /**
     * Icon Cls class
     */
    export let iconCls = "";

    export let iconText = "";

    /**
     * Element Ripple
     */
    export let ripple = true;

    export let ui = "primary";

    export let fullWidth = false;

    export let pressed = false;

    export let itemId = "";

    export let floating = false;
</script>

<div
    class="btn-container"
    class:w-100-percent={fullWidth}
    class:pressed
    class:floating
>
    <button
        class="btn-el {cls} btn-el-{ui} flex-cont flex-vh"
        class:ripple
        class:w-100-percent={fullWidth}
        rec-id={itemId}
        on:click
    >
        {#if iconCls && (iconAlign === "left" || iconAlign === "top")}
            <span class="btn-icon icon-{iconAlign} {iconCls}">{iconText}</span>
        {/if}
        <span class="btn-text">
            {text}
        </span>
        {#if iconCls && (iconAlign === "right" || iconAlign === "bottom")}
            <span class="btn-icon icon-{iconAlign} {iconCls}">{iconText}</span>
        {/if}
    </button>
</div>

<style>
    .btn-container {
        padding: 0 8px;
    }

    .btn-el {
        position: relative;
        overflow: hidden;
        transition: background 400ms;
        color: var(--btn-color);
        background-color: var(--btn-background);
        padding: 10px 15px;
        font-family: "Roboto", sans-serif;
        font-size: 15px;
        outline: 0;
        border: 0;
        border-radius: 0.25rem;
        box-shadow: 0 0 0.5rem rgb(0 0 0 / 30%);
        cursor: pointer;
    }

    .menu-el {
        color: var(--btn-color);
        background-color: var(--btn-background);
    }

    /* Ripple effect */
    .ripple {
        background-position: center;
        transition: background 0.8s;
    }
    .ripple:hover {
        background: var(--btn-background)
            radial-gradient(circle, transparent 1%, #47a7f5 1%) center/15000%;
    }
    .ripple:active {
        background-color: var(--btn-background);
        background-size: 100%;
        transition: background 0s;
    }

    .icon-top,
    .icon-bottom {
        display: block;
    }

    .btn-el-cancel {
        background-color: #b0b0b0;
    }
    .btn-el-delete {
        background: var(--required);
    }
    .btn-el-action {
        background-color: transparent !important;
        color: var(--btn-background) !important;
    }
</style>
