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
    export let placeholder = "";

    let cmpId = "passfield-" + ++id;

    const dispatch = createEventDispatcher();

    function onkeydown(e) {
        if (e.keyCode == 13) {
            dispatch("enter", value);
        }
    }
    let showPassword = false;

    function togglePasswordVisibility() {
        showPassword = !showPassword;
    }
</script>
<div class="email-container margin-top-20">
<!-- <div class="field-container text-field flex-cont flex-dir-column"> -->
    <!-- <label class="field-label" for={cmpId} width="{labelWidth}px"
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    > -->
    <span>
        <i class="fa fa-lock" style="color: #1a9b97;"></i>
    </span>
    {#if showPassword}
    <input
        type="text"
        id={cmpId}
        {name}
        {required}
        {placeholder}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        on:keydown={onkeydown}
    />
{:else}
    <input
        type="password"
        id={cmpId}
        {name}
        {required}
        {placeholder}
        minlength={minLength}
        maxlength={maxLength}
        bind:value
        on:keydown={onkeydown}
    />
{/if}
    <span>
        <i class="fa" class:fa-eye={showPassword} class:fa-eye-slash={!showPassword} title="Toggle password visibility" style="color: #1a9b97;" on:click={togglePasswordVisibility}></i>
    </span>
</div>

<style>
</style>
