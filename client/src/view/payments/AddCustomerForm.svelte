<script>
    import SessionUtil from "../../util/SessionUtil";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Utils from "../../util/Utils";
    import { createEventDispatcher } from "svelte";
    import axios from "axios";
  
    let customer_email = "";
    let customer_phone = "";
    let customer_name = "";
    let customer_bank_account_number = "";
    let customer_bank_ifsc = "";
    let customer_bank_code = "";
  
    let isProcessing = false;
    let errors = {};
    let userInfo = {};
  
    const dispatch = createEventDispatcher();
  
    $: userInfo = SessionUtil.get("info", true);
  
    function validateField(field, value) {
      switch (field) {
        case "customer_email":
          errors.customer_email = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
            ? ""
            : "Enter a valid email address.";
          break;
        case "customer_phone":
          errors.customer_phone = /^\d{10}$/.test(value)
            ? ""
            : "Phone must be 10 digits.";
          break;
        case "customer_name":
          errors.customer_name = value.trim() ? "" : "Customer name is required.";
          break;
        case "customer_bank_account_number":
          errors.customer_bank_account_number = /^\d{9,18}$/.test(value)
            ? ""
            : "Account number must be between 9–18 digits.";
          break;
        case "customer_bank_ifsc":
          errors.customer_bank_ifsc = /^[A-Z]{4}0[A-Z0-9]{6}$/.test(value)
            ? ""
            : "Enter a valid IFSC code.";
          break;
        case "customer_bank_code":
          errors.customer_bank_code = value.trim() ? "" : "Customer Bank code is required.";
          break;
      }
    }
  
    function validateForm() {
      validateField("customer_email", customer_email);
      validateField("customer_phone", customer_phone);
      validateField("customer_name", customer_name);
      validateField("customer_bank_account_number", customer_bank_account_number);
      validateField("customer_bank_ifsc", customer_bank_ifsc);
      validateField("customer_bank_code", customer_bank_code);
  
      return Object.values(errors).every((e) => !e);
    }
  
    async function submitBankDetails(e) {
      e.preventDefault();
      if (!validateForm()) return;
  
      const obj = {
        userId: userInfo?.userId,
        customer_email,
        customer_phone,
        customer_name,
        customer_bank_account_number,
        customer_bank_ifsc,
        customer_bank_code,
      };
  
      console.log("Submitting bank details:", obj);
      isProcessing = true;
  
      try {
        const res = await axios.post(urlConst.save_bank_details, obj, {
          headers: Request.getHeaders(null),
          timeout: 120000,
        });
  
        console.log("response:", res.data);
        resetForm();
        isProcessing = false;
        setTimeout(() => dispatch("bankDetailsSaved"), 1000);
      } catch (err) {
        Utils.log(err);
        isProcessing = false;
      }
    }
  
    function resetForm() {
      customer_email = "";
      customer_phone = "";
      customer_name = "";
      customer_bank_account_number = "";
      customer_bank_ifsc = "";
      customer_bank_code = "";
      errors = {};
    }
  </script>
  
  <form class="bank-form scrollable-form" on:submit={submitBankDetails} novalidate>
    <fieldset>
      <legend>Customer Bank Details</legend>
  
      <div class="form-grid">
        <div class="form-group" title="Enter the registered email ID of the customer.">
          <label for="customer_email">Customer Email *</label>
          <input
            id="customer_email"
            type="email"
            bind:value={customer_email}
            placeholder="e.g. john@cashfree.com"
            aria-invalid={errors.customer_email ? "true" : "false"}
            on:input={(e) => validateField("customer_email", e.target.value)}
          />
          {#if errors.customer_email}
            <span class="error-message">{errors.customer_email}</span>
          {/if}
        </div>
  
        <div class="form-group" title="Enter the 10-digit phone number.">
          <label for="customer_phone">Customer Phone *</label>
          <input
            id="customer_phone"
            type="number"
            bind:value={customer_phone}
            placeholder="e.g. 9908734801"
            aria-invalid={errors.customer_phone ? "true" : "false"}
            on:input={(e) => validateField("customer_phone", e.target.value)}
          />
          {#if errors.customer_phone}
            <span class="error-message">{errors.customer_phone}</span>
          {/if}
        </div>
  
        <div class="form-group" title="Enter the full name of the customer.">
          <label for="customer_name">Customer Name *</label>
          <input
            id="customer_name"
            type="text"
            bind:value={customer_name}
            placeholder="e.g. John Doe"
            aria-invalid={errors.customer_name ? "true" : "false"}
            on:input={(e) => validateField("customer_name", e.target.value)}
          />
          {#if errors.customer_name}
            <span class="error-message">{errors.customer_name}</span>
          {/if}
        </div>
  
        <div class="form-group" title="Enter the customer's bank account number.">
          <label for="customer_bank_account_number">Account Number *</label>
          <input
            id="customer_bank_account_number"
            type="number"
            bind:value={customer_bank_account_number}
            placeholder="e.g. 1518121112"
            aria-invalid={errors.customer_bank_account_number ? "true" : "false"}
            on:input={(e) => validateField("customer_bank_account_number", e.target.value)}
          />
          {#if errors.customer_bank_account_number}
            <span class="error-message">{errors.customer_bank_account_number}</span>
          {/if}
        </div>
  
        <div class="form-group" title="Enter the IFSC code of the bank branch.">
          <label for="customer_bank_ifsc">IFSC Code *</label>
          <input
            id="customer_bank_ifsc"
            type="text"
            bind:value={customer_bank_ifsc}
            placeholder="e.g. XITI0000001"
            maxlength="11"
            style="text-transform: uppercase"
            aria-invalid={errors.customer_bank_ifsc ? "true" : "false"}
            on:input={(e) => validateField("customer_bank_ifsc", e.target.value.toUpperCase())}
          />
          {#if errors.customer_bank_ifsc}
            <span class="error-message">{errors.customer_bank_ifsc}</span>
          {/if}
        </div>
  
        <div class="form-group" title="Provide the bank code (e.g. SBIB, SBNCORP, YESB). Required for net banking payments, if you want to do a bank account check (TPV)">
          <label for="customer_bank_code">Customer Bank Code *</label>
          <input
            id="customer_bank_code"
            type="text"
            bind:value={customer_bank_code}
            placeholder="e.g. SBIB, SBNCORP, YESB"
            aria-invalid={errors.customer_bank_code ? "true" : "false"}
            on:input={(e) => validateField("customer_bank_code", e.target.value)}
          />
          {#if errors.customer_bank_code}
            <span class="error-message">{errors.customer_bank_code}</span>
          {/if}
        </div>
      </div>
  
      <button type="submit" disabled={isProcessing}>
        {isProcessing ? "Saving..." : "Save Bank Details"}
      </button>
    </fieldset>
  </form>
  
  <style>
    .bank-form {
      background-color: #fff;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
      margin: 0 auto;
      max-height: 80vh;
      overflow-y: auto;
    }
    *{
        overflow-y: auto;
    }
  
    .scrollable-form::-webkit-scrollbar {
      width: 6px;
    }
  
    .scrollable-form::-webkit-scrollbar-thumb {
      background: #ccc;
      border-radius: 4px;
    }
  
    fieldset {
      border: none;
      padding: 0;
    }
  
    legend {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 16px;
      color: #333;
    }
  
    /* ✅ Responsive grid layout */
    .form-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 20px;
    }
  
    @media (min-width: 1024px) {
      .form-grid {
        grid-template-columns: 1fr 1fr;
      }
    }
  
    .form-group {
      display: flex;
      flex-direction: column;
      position: relative;
    }
  
    label {
      font-weight: 600;
      margin-bottom: 6px;
      color: #374151;
    }
  
    input {
      padding: 10px 12px;
      font-size: 16px;
      border: 1px solid #ddd;
      border-radius: 6px;
      transition: all 0.2s ease;
    }
  
    input:focus {
      border-color: #1a9b97;
      box-shadow: 0 0 0 2px rgba(26, 155, 151, 0.2);
    }
  
    input[aria-invalid="true"] {
      border-color: #dc2626;
    }
  
    .error-message {
      color: #dc2626;
      font-size: 13px;
      margin-top: 4px;
    }
  
    button {
      margin-top: 20px;
      padding: 14px;
      font-size: 16px;
      color: #fff;
      background-color: #1a9b97;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      width: 100%;
      font-weight: bold;
      transition: background 0.2s;
    }
  
    button:disabled {
      background-color: #bbb;
    }
  
    button:hover:not(:disabled) {
      background-color: #14877e;
    }
  
    input[type="number"]::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }
  
    @media (min-width: 768px) {
      .bank-form {
        width: 90%;
      }
    }
  
    @media (min-width: 1200px) {
      .bank-form {
        width: 90%;
      }
    }
  </style>
  