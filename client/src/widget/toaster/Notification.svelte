<!-- Toast.svelte -->

<script>
    import { onMount } from "svelte";
  
    export let message = "";
    export let isOpen = false;
  
    function closeToast() {
      isOpen = false;
    }
  
    // Listen for custom events to open the toast
    onMount(() => {
      window.addEventListener("show-toast", (event) => {
        message = event.detail.message;
        isOpen = true;
        // Close the toast after a certain duration (e.g., 3000 milliseconds)
        setTimeout(() => {
          closeToast();
        }, 3000);
      });
    });
  </script>
  
  {#if isOpen}
    <div class="test">
      <button on:click={closeToast} class="close-btn" title="close">×</button>
      <div class="messgae">
        <i class="fa fa-info-circle fa-lg pointer my-auto info-styles"></i>
        <p class="my-auto">{message}</p>
      </div>
      
    </div>
  {/if}
  
  <style>
    .test {
      position: absolute;
      top: 40px;
      right: 45%;
      width: 350px;
      background-color: #1a9b97;
      color: #fff;
      padding: 10px 15px 15px 15px;
      border-radius: 5px;
      margin: 0 auto;
      display: flex;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    .messgae{
        display: flex;
        margin-top: 15px;
    }
    .info-styles{
        color: white;
        margin-top: 0;
        margin-right: 10px;
    }
  
    .close-btn {
        position: absolute;
      background: none;
      border: none;
      color: #fff;
      font-size: 18px;
      cursor: pointer;
      top: 5px;
      right: 10px;
      outline: none;
    }
  </style>
  