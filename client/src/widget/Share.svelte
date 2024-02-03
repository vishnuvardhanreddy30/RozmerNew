<!-- Modal.svelte -->

<script>
    export let title;
    export let isOpen;
    export let onClose;
    export let postData;
    let postURL;
    $: {
    console.log("postgfahgs : ", postData)
    postURL = 'https://localhost:3000/#home?pid='+postData?.postId
    }
  
    $: modalStyle = `display: ${isOpen ? 'block' : 'none'}`;
  
    function closeModal() {
      onClose();
    }

    function shareOnFacebook() {
    const shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(postURL)}`;
    openSharePopup(shareUrl);
  }

  function shareOnTwitter() {
    const shareUrl = `https://twitter.com/intent/tweet?url=${encodeURIComponent(postURL)}&text=${encodeURIComponent(postData.title)}`;
    openSharePopup(shareUrl);
  }

  function shareOnWhatsApp() {
    const shareUrl = `https://api.whatsapp.com/send?text=${encodeURIComponent(postData.title + ' ' + postURL)}`;
    openSharePopup(shareUrl);
  }

  function shareOnLinkedIn() {
    const shareUrl = `https://www.linkedin.com/shareArticle?mini=true&url=${encodeURIComponent(postURL)}&title=${encodeURIComponent(postData.title)}`;
    openSharePopup(shareUrl);
  }

  function openSharePopup(shareUrl) {
    window.open(shareUrl, '_blank', 'width=600,height=400');
  }
  const copyToClipboard = async() => {
    try {
      // Use the Clipboard API to write text to the clipboard
      await navigator.clipboard.writeText(postURL);
      console.log('Text copied to clipboard successfully');
    } catch (err) {
      console.error('Unable to copy text to clipboard', err);
    }
  }
  </script>
  
  <div class="modal" style={modalStyle}>
    <div class="modal-content">
      <span class="close" on:click={closeModal}>&times;</span>
      <span class="modal-title">{title}</span>
      <div class="copy-link" on:click={copyToClipboard}>Copy Link</div>
      <span class="or-text">(or)</span>
      <div class="social-sharing-buttons">
        <span class="share-button" on:click={() => shareOnFacebook()}>
          <i class="fa fa-facebook fa-lg share-button1"></i>
        </span>
        <span class="share-button" on:click={() => shareOnTwitter()}>
          <i class="fa fa-twitter fa-lg share-button1"></i>
        </span>
        <span class="share-button" on:click={() => shareOnWhatsApp()}>
          <i class="fa fa-whatsapp fa-lg share-button1"></i>
        </span>
        <span class="share-button" on:click={() => shareOnLinkedIn()}>
          <i class="fa fa-linkedin fa-lg share-button1"></i>
        </span>
        <!-- Add more social media buttons as needed -->
      </div>
    </div>
  </div>
  
  <style>
    .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .modal-content {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    position: relative;
    margin-top: 40px;
  }

  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    /* font-size: 30px; */
    height: 30px;
    width: 30px;
    cursor: pointer;
    border: 1px solid black;
    border-radius: 50%;
    text-align: center;
  }
  .modal-title{
    font-size: 24px;
    text-align: center;
    font-weight: 500;
  }
  .or-text{
    text-align: center;
    margin: 6px 0;
  }
  .copy-link{
    margin-top: 40px;
    border: 1px solid var(--primary-color);
    background-color: var(--body-bg-color);
    padding: 6px;
    font-size: 16px;
    border-radius: 10px;
    color: var(--primary-color);
    font-weight: 500;
    text-align: center;
    cursor: pointer;
  }
  .share-button{
    border: 1px solid var(--primary-color);
    background-color: var(--body-bg-color);
    padding: 6px;
    color: var(--primary-color);
    border-radius: 10px;
    text-align: center;
    cursor: pointer;
  }
  .share-button1{
    width: 30px !important;
    height: 24px !important;
  }
    /* ... existing styles ... */
  
    .social-sharing-buttons {
    margin: 0 auto;
    width: 60%;
    display: flex;
    justify-content: space-around;
  }
  </style>
  