<script context="module">
    let id = -1;
</script>

<script>
    export let cls = '';
    let cmpId = 'formcontainer-'+ (++id);

    function getFormEl() {
        return document.getElementById(cmpId)
    }

    export function isValid() {
        return getFormEl().checkValidity();
    }

    export function getValues(){
        let formData = new FormData(getFormEl());

        return Object.fromEntries(formData);
    }

    function ignoreSubmit(){
        return false;
    }
</script>

<form id={cmpId} on:submit|preventDefault={ignoreSubmit} class="form-container {cls}">
    <div class="form-header">
        <slot name="header"></slot>
    </div>
    <div class="form-error">
        <slot name="error"></slot>
    </div>
    <div class="form-body">
        <slot name="body"></slot>
    </div>

    <div class="form-footer">
        <slot name="footer"></slot>
    </div>

</form>

<style>
    .form-error {
        color: red;
        font-size: 16px;
        text-align: center;
        max-width: 300px;
        word-break: break-word;
        font-weight: bold;
    }
</style>
