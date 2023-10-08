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
    export let placeholder = "";

    let cmpId = "textfield-" + ++id;

    function handleMaxMin(e) {
        if (value != undefined && value.toString().length >= maxLength) {
            e.preventDefault();
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
    <span>
        <i class="fa fa-phone" style="color: #1a9b97;"></i>
    </span>
    <input
        type="number"
        id={cmpId}
        {name}
        {required}
        {placeholder}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        on:keypress={handleMaxMin}
    />
</div>

<style>
</style>
