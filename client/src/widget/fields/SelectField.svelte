<script context="module">
    let id = -1;
</script>

<script>
    import { onMount } from "svelte";
    import { createEventDispatcher } from "svelte";
    import Request from "../../util/Request";
    import Utils from "../../util/Utils";

    const dispatch = createEventDispatcher();

    export let value = "";
    export let mode = 0; // 0: local /  1: Server
    export let url = "";
    export let options = [];
    export let disabled = false;
    export let params = {};
    export let rootProperty = "data";
    export let method = "get";
    export let valueField = "value";
    export let nameField = "name";
    export let labelWidth = 100;
    export let label = "";
    export let labelSeperator = "";
    export let autoSelect = 0; // 0 - false 1- true
    export let labelAlign = "top";
    export let labelCls = "";
    export let required;

    let cmpId = "selectfield-" + ++id;

    function onSuccess(data) {
        if (rootProperty) {
            data = data[rootProperty];
        }

        options = data;
        if (+autoSelect && data && data.length) {
            value = data[0][valueField];

            dispatch("change", {
                cmp: this,
                value: value,
                options: options[0],
            });
        }
        disabled = false;

        dispatch("load", {
            success: true,
            data: data,
        });
    }

    function onFailure(e) {
        dispatch("load", {
            success: false,
            e: e,
        });

        options = [];
        disabled = false;
    }

    onMount(() => {
        if (+mode === 0) {
            return;
        }

        if (!url) {
            Utils.log("No URL available");
        }

        disabled = true;

        Request[method](url, params, onSuccess, onFailure, onSuccess);

        dispatch("load", {
            loading: true,
        });
    });

    function onChange() {
        dispatch("change", {
            cmp: this,
            value: value,
            options: options[this.options.selectedIndex],
        });
    }
</script>

<div class="field-container select-field">
    <label
        class="field-label label-{labelAlign} {labelCls}"
        for={cmpId}
        width={labelWidth}
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    >

    <select bind:value on:change={onChange} class:disabled autoSelect="false">
        {#if !Utils.isEmpty(options)}
            {#each options as option}
                <option value={option[valueField]}>{option[nameField]}</option>
            {/each}
        {/if}
    </select>
</div>
