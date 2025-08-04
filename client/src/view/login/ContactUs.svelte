<script>
// @ts-nocheck

    import { createEventDispatcher } from "svelte";
    import Utils from "../../util/Utils";
    import TextField from "../../widget/fields/TextField.svelte";
    import EmailField from "../../widget/fields/EmailField.svelte";
    import TextAreaField from "../../widget/fields/TextAreaField.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Labels from "../../const/Labels";
    import logo from "../../assets/logo.png";
  
    const dispatch = createEventDispatcher();
  
    let required = true,
      fullWidth = true,
      name = "",
      email = "",
      message = "",
      showSuccessMessage = false;
  
    function onSubmit() {
      if (Utils.isEmpty(name) || Utils.isEmpty(email) || Utils.isEmpty(message)) {
        return Utils.alert("All fields are required", Labels.alert.info);
      }
  
      console.log("Form submitted:", { name, email, message });
  
      showSuccessMessage = true;
  
      setTimeout(() => {
        showSuccessMessage = false;
      }, 5000);
  
      name = email = message = "";
    }
  </script>
  
  <div class="login-cont flex-cont wh-100-percent">
    <div class="login-left flex-1">
      <img src={logo} alt="logo" class="sub-logo" />
    </div>
  
    <div class="login-right flex-1 flex-cont flex-vh bg-standard">
      <div class="margin-bottom-to-child-15 p2 login-items">
        <img src={logo} alt="logo" class="sub-logo logo-hidden" />
  
        <p class="heading text-center">CONTACT US</p>
  
        {#if showSuccessMessage}
          <div class="success-msg">
            Form submitted successfully! Our support team will reach out to you within 24 hours.
          </div>
        {/if}
  
        <TextField label="Full Name" bind:value={name} {required} placeholder="Your Name" />
        <EmailField label="Email Address" bind:value={email} {required} placeholder="Your Email" />
        <TextAreaField
            label=""
            name="message"
            bind:value={message}
            placeholder="Your message here..."
            required
        />
  
        <Button text="Send Message" on:click={onSubmit} {fullWidth} />
  
        <p class="terms-text">
          Our
          <a href="/terms_and_conditions.docx" target="_blank" rel="noopener noreferrer">
            Terms and Conditions
          </a>.
        </p>
      </div>
  
      <p class="copy-text text-right">Copyright &copy; 2023 Rozmer. All rights reserved.</p>
    </div>
  </div>
  
  <style>
    .success-msg {
      background-color: #dff0d8;
      color: #3c763d;
      padding: 1rem;
      border-radius: 8px;
      text-align: center;
      margin-bottom: 1rem;
    }
  
    .heading {
      font-size: 1.5rem;
      font-weight: bold;
      margin: 1rem 0;
      color: white;
    }
  
    .login-items {
      width: 100%;
      max-width: 500px;
    }
  
    .terms-text {
      margin-top: 1rem;
      font-size: 0.9rem;
      color: #fff;
    }
  
    .terms-text a {
      color: #1a9b97;
      text-decoration: underline;
    }
  </style>
  