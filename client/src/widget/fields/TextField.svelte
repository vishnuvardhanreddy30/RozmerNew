<svelte:options accessors />

<script context="module">
    let id = -1;
</script>

<script>
    import { createEventDispatcher } from "svelte";

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
    export let autofocus = false;

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
</script>

<div class="field-container text-field flex-cont flex-dir-{labelAlign} {cls}">
    <label class="field-label" for={cmpId} width={labelWidth}
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    >

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
</div>

<style>
</style>
