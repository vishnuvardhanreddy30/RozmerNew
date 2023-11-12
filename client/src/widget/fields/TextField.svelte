<svelte:options accessors />

<script context="module">
    let id = -1;
</script>

<script>
    import { createEventDispatcher } from "svelte";
    import Button from "../../widget/button/Button.svelte";

    export let label = "";
    export let name = "";
    export let required = false;
    export let minLength = Infinity;
    export let maxLength = Infinity;
    export let labelSeperator = "";
    export let labelWidth = 100;
    export let value = "";
    export let cls = "";
    export let labelAlign = "column"; // top, left
    export let placeholder = "";
    export let fieldtype = "";
    export let autofocus = false;
    let searchVal;

    let searchIconText = "refresh";

    let inputEl;

    export function focus() {
        inputEl.focus();
    }

    let cmpId = "textfield-" + ++id;

    const dispatch = createEventDispatcher();

    function onkeydown(e) {
        if (e.keyCode == 13) {
            dispatch("enter", value);
        }
    }
    $: {
        if (searchVal) {
            searchIconText = "search";
        } else {
            searchIconText = "refresh";
        }
    }
</script>

<!-- <div class="field-container text-field flex-cont flex-dir-{labelAlign} {cls}"> -->
<div class="email-container">
    <!-- <label class="field-label" for={cmpId} width={labelWidth}
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    > -->
    {#if !fieldtype}
    <span>
        <i class="fa fa-user" style="color: #1a9b97;"></i>
    </span>
    {/if}
    <input
        id={cmpId}
        {name}
        {required}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        {placeholder}
        {autofocus}
        on:keydown={onkeydown}
        bind:this={inputEl}
    />
    {#if fieldtype === 'search'}
    <span class="pointer" title={fieldtype} on:click={dispatch("enter", value)}>
        <i class="fa fa-search" style="color: #1a9b97;"></i>
    </span>
    {/if}
</div>

<style>
</style>
