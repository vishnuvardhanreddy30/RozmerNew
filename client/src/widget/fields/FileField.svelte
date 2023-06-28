<svelte:options accessors />

<script context="module">
    let id = -1;
</script>

<script>
    import Utils from "../../util/Utils";


    export let label = "";
    export let name = "";
    export let required = false;
    export let minLength = Infinity;
    export let maxLength = Infinity;
    export let labelSeperator = "";
    export let labelWidth = 100;
    export let files;
    export let cls = "";
    export let labelAlign = "column"; // top, left

    let fieldInput;

    $: {
        if (files) {
            // Note that `files` is of type `FileList`, not an Array:
            // https://developer.mozilla.org/en-US/docs/Web/API/FileList
            Utils.log(files);

            for (const file of files) {
                Utils.log(`${file.name}: ${file.size} bytes`);
            }
        } else {
            if(fieldInput){
                fieldInput.value = null;
            }
        }
    }

    let cmpId = "textfield-" + ++id;
</script>

<div class="field-container text-field flex-cont flex-dir-{labelAlign} {cls}">
    <label class="field-label custom-file-upload" for={cmpId} width={labelWidth}
        ><span class="material-icons file-upload">file_upload</span> {label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}

    <input
        id={cmpId}
        {name}
        {required}
        minlength={minLength}
        maxlength={maxLength}
        accept="image/png, image/jpeg"
        bind:files
        type="file"
        bind:this={fieldInput}
    />
    </label>
</div>

<style>
    input[type="file"] {
        display: none;
    }
    .custom-file-upload {
        border: 1px solid #ccc;
        display: inline-block;
        padding: 6px 12px !important;
        cursor: pointer;
    }

    .file-upload {
        font-size: 18px;
        vertical-align: text-top;
    }
</style>
