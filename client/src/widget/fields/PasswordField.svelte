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

    let cmpId = "passfield-" + ++id;

    const dispatch = createEventDispatcher();

    function onkeydown(e) {
        if (e.keyCode == 13) {
            dispatch("enter", value);
        }
    }
</script>

<div class="field-container text-field flex-cont flex-dir-column">
    <label class="field-label" for={cmpId} width="{labelWidth}px"
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    >

    <input
        type="password"
        id={cmpId}
        {name}
        {required}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        on:keydown={onkeydown}
    />
</div>

<style>
</style>
