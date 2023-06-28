<svelte:options accessors />

<script context="module">
    let id = -1;
</script>

<script>
    export let label = "";
    export let name = "";
    export let required = false;
    export let minLength = Infinity;
    export let maxLength = Infinity;
    export let labelSeperator = "";
    export let labelWidth = 100;
    export let value = "";
    export let labelAlign = "column"; // top, left
    export let cls = "";

    let cmpId = "textfield-" + ++id;

    function handleMaxMin(e) {
        if (value != undefined && value.toString().length >= maxLength) {
            e.preventDefault();
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
        type="number"
        id={cmpId}
        {name}
        {required}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        on:keypress={handleMaxMin}
    />
</div>

<style>
</style>
