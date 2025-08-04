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
  export let disabled = false;

  let textareaEl;
  let cmpId = "textarea-" + ++id;

  const dispatch = createEventDispatcher();

  export function focus() {
    textareaEl.focus();
  }

  function onkeydown(e) {
    if (e.keyCode === 13 && !e.shiftKey) {
      e.preventDefault();
      dispatch("enter", value);
    }
  }
</script>

<div class="textarea-container text-field flex-cont flex-dir-{labelAlign} {cls}">
  {#if label}
    <label for={cmpId} class="field-label" style="width: {labelWidth}px">
      {label}
      {#if required}
        <span class="req-lbl">*</span>
      {/if}
      {labelSeperator}
    </label>
  {/if}

  <textarea
    id={cmpId}
    {name}
    {required}
    minlength={minLength}
    maxlength={maxLength}
    bind:value
    {placeholder}
    {autofocus}
    {disabled}
    autocomplete="off"
    on:keydown={onkeydown}
    bind:this={textareaEl}
    rows="4"
  />
</div>

<style>
  .textarea-container {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin: 0.5rem 0;
  }

  .field-label {
    font-weight: 500;
    color: #333;
  }

  .req-lbl {
    color: red;
    margin-left: 4px;
  }

  textarea {
    width: 100%;
    padding: 0.75rem;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 6px;
    resize: vertical;
    box-sizing: border-box;
    font-family: inherit;
  }

  textarea:disabled {
    background: #f9f9f9;
    color: #999;
  }
</style>
