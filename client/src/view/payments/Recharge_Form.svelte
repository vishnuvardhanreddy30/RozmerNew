<script>
  import SessionUtil from "../../util/SessionUtil";
  import Request from "../../util/Request";
  import Utils from "../../util/Utils";
  import { createEventDispatcher } from "svelte";

  const dispatch = createEventDispatcher();
  let userInfo = {};
  $: userInfo = SessionUtil.get("info", true);

  let existingAccounts = [
    {
      id: 1,
      customer_email: "john@cashfree.com",
      customer_phone: "9908734801",
      customer_name: "John Doe",
      customer_bank_account_number: "1518121112",
      customer_bank_ifsc: "XITI0000001",
      customer_bank_code: 3333,
      customer_uid: "54deabb4-ba45-4a60-9e6a-9c016fe7ab10",
    },
    {
      id: 2,
      customer_email: "mark@cashfree.com",
      customer_phone: "9823342222",
      customer_name: "Mark Smith",
      customer_bank_account_number: "2020202020",
      customer_bank_ifsc: "HDFC0000999",
      customer_bank_code: 1234,
      customer_uid: "a7deabb4-ba45-4a60-9e6a-9c016fe7cc99",
    },
  ];

  let selectedAccountId = null;
  let orderAmount = "";
  let orderCurrency = "";
  let otp = "";
  let otpDigits = ["", "", "", "", "", ""];
  let showOtpSection = false;
  let otpVerified = false;
  let errors = {};
  let timer = 30;
  let canResend = false;
  let timerInterval;

  const currencyOptions = ["INR", "USD", "EUR"];

  const handleSelectAccount = (id) => selectedAccountId = id;

  const handleDelete = (id) => {
    if (confirm("Are you sure you want to delete this bank account?")) {
      existingAccounts = existingAccounts.filter(a => a.id !== id);
      if (selectedAccountId === id) selectedAccountId = null;
    }
  };

  const maskBankNumber = (number) => {
    if (!number) return "";
    const len = number.length;
    if (len <= 4) return number;
    return number.slice(0, 4) + "X".repeat(len - 6) + number.slice(-2);
  };

  const viewAddCustomerForm = () => dispatch("viewAddCustomerForm");

  function validateForm() {
    errors = {};
    if (!selectedAccountId) errors.selectedAccountId = "Please select a bank account.";
    if (!orderCurrency) errors.orderCurrency = "Please select a currency.";
    if (!orderAmount || Number(orderAmount) <= 0)
      errors.orderAmount = "Please enter a valid amount.";
    if (showOtpSection && otpDigits.join("").length < 6)
      errors.otp = "Please enter a 6-digit OTP.";
    return Object.keys(errors).length === 0;
  }

  const startTimer = () => {
    timer = 30;
    canResend = false;
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
      timer--;
      if (timer <= 0) {
        clearInterval(timerInterval);
        canResend = true;
      }
    }, 1000);
  };

  const handleResendOtp = () => {
    alert("New OTP sent!");
    otpDigits = ["", "", "", "", "", ""];
    startTimer();
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;

    if (!showOtpSection && !otpVerified) {
      showOtpSection = true;
      alert("OTP sent to your registered email/phone!");
      startTimer();
      return;
    }

    if (showOtpSection && !otpVerified) {
      otp = otpDigits.join("");
      console.log("otp", otp)
      if (otp === "123456") {
        otpVerified = true;
        alert("✅ OTP verified successfully!");
      } else {
        errors.otp = "Invalid OTP. Please try again.";
        return;
      }
    }

    const account = existingAccounts.find(a => a.id === selectedAccountId);
    const payload = {
      customer_id: account.customer_uid,
      bank_details_id: account.id,
      order_currency: orderCurrency,
      order_amount: Number(orderAmount),
      userId: userInfo?.userId,
    };

    console.log("✅ Final Payment Payload:", payload);
    alert("Payment submitted successfully!");
  };

  const handleOtpInput = (index, event) => {
    const value = event.target.value.replace(/\D/g, "");
    if (value.length > 1) return;
    otpDigits[index] = value;
    if (value && index < 5) {
      const next = document.getElementById(`otp-${index + 1}`);
      next?.focus();
    }
  };
</script>

<form class="bank-form scrollable-form" on:submit|preventDefault={handleSubmit}>
  <!-- 🏦 Existing Accounts Section -->
  {#if !showOtpSection}
  <fieldset class="space-y-4">
    <legend class="label-text">Select Bank Account</legend>

    {#if existingAccounts.length > 0}
      {#each existingAccounts as account (account.id)}
        <div class="bank-details-card flex-cont flex-col border rounded-xl px-4 mb-2 shadow-sm hover:shadow-md transition bg-gray-50 space-between">
          <label class="flex-cont flex-col cursor-pointer" on:click={() => handleSelectAccount(account.id)}>
            <input type="radio" name="account" bind:group={selectedAccountId} value={account.id} class="mt-1" />
            
            <div class="grid grid-cols-2 gap-4 mt-2 ml-3">
              <div><strong>Name:</strong> {account.customer_name}</div>
              <div><strong>Account Number:</strong> {maskBankNumber(account.customer_bank_account_number)}</div>
            </div>
            
            <div class="grid grid-cols-2 gap-4 mt-2 ml-5 text-sm text-gray-600">
              <div><strong>IFSC Code:</strong> {account.customer_bank_ifsc}</div>
              <div><strong>Bank Code:</strong> {account.customer_bank_code}</div>
            </div>
          </label>
          <button type="button" on:click={() => handleDelete(account.id)} class="delete-account" title="Delete Bank Account">🗑️</button>
        </div>
      {/each}
    {:else}
      <div class="bank-details-card rounded-xl p-6 text-center bg-gray-50 text-gray-600 shadow-sm mb-3">
        <p class="text-lg font-medium mb-2">No bank details found</p>
        <button type="button" class="add-bank-details-btn" on:click={viewAddCustomerForm}>Add Bank Details</button>
      </div>
    {/if}

    {#if errors.selectedAccountId}
      <span class="error-message">{errors.selectedAccountId}</span>
    {/if}
  </fieldset>

  <!-- 💰 Payment Details Section -->
  <fieldset class="bg-gray-50 shadow-inner rounded-xl space-y-4 mt-4">
    <legend class="label-text">Payment Details</legend>
    <div class="form-grid">
      <div class="form-group">
        <label for="orderCurrency">Currency *</label>
        <select id="orderCurrency" bind:value={orderCurrency}>
          <option value="">Select Currency</option>
          {#each currencyOptions as cur}
            <option value={cur}>{cur}</option>
          {/each}
        </select>
        {#if errors.orderCurrency}
          <span class="error-message">{errors.orderCurrency}</span>
        {/if}
      </div>
      <div class="form-group">
        <label for="orderAmount">Amount *</label>
        <input id="orderAmount" type="number" bind:value={orderAmount} placeholder="Enter amount" />
        {#if errors.orderAmount}
          <span class="error-message">{errors.orderAmount}</span>
        {/if}
      </div>
    </div>
  </fieldset>
  {/if}
    

  <!-- 🔐 OTP Section -->
  {#if showOtpSection}
    <fieldset class="bg-gray-50 shadow-inner rounded-xl space-y-4 mt-4">
      <legend class="label-text">OTP Verification</legend>
      <div class="otp-container">
        {#each otpDigits as digit, i}
          <input
            id={"otp-" + i}
            class="otp-box"
            type="text"
            maxlength="1"
            bind:value={otpDigits[i]}
            on:input={(e) => handleOtpInput(i, e)}
          />
        {/each}
      </div>

      {#if errors.otp}
        <span class="error-message">{errors.otp}</span>
      {/if}

      <div class="otp-footer">
        {#if canResend}
          <button type="button" class="resend-btn" on:click={handleResendOtp}>Resend OTP</button>
        {:else}
          <p class="timer-text">Resend OTP in {timer}s</p>
        {/if}
      </div>
    </fieldset>
  {/if}

  <!-- 🚀 Submit Button -->
  <div class="pt-2">
    <button type="submit" class="submit-btn">
      {#if showOtpSection && !otpVerified}
        Verify OTP
      {:else if otpVerified}
        Confirm Payment
      {:else}
        Submit Payment
      {/if}
    </button>
  </div>
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

  .label-text{
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 6px;
  }

  .bank-details-card { border-radius: 12px; padding: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.08); }

  .form-grid { display: grid; grid-template-columns: 1fr; gap: 16px; }
  @media (min-width: 1024px) { .form-grid { grid-template-columns: 1fr 1fr; } }

  .form-group { display: flex; flex-direction: column; }
  label { font-weight: 600; margin-bottom: 6px; color: #374151; }
  input, select { padding: 10px 12px; font-size: 16px; border: 1px solid #ddd; border-radius: 6px; transition: all 0.2s ease; }
  input:focus, select:focus { border-color: #1a9b97; box-shadow: 0 0 0 2px rgba(26, 155, 151, 0.2); }

  .error-message { color: #dc2626; font-size: 13px; margin-top: 4px; }

  .submit-btn {
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
  .submit-btn:hover { background-color: #14877e; }

  .add-bank-details-btn {
    margin-top: 10px;
    padding: 14px;
    font-size: 16px;
    color: #fff;
    background-color: #1a9b97;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    width: 300px;
    font-weight: bold;
    transition: 0.2s;
  }
  .add-bank-details-btn:hover { background-color: #14877e; }


  .delete-account { border-radius: 50%; padding: 4px; border: none; height: 50px; width: 50px;}

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

  /* 🆕 OTP UI Additions */
  .otp-container {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 10px;
  }

  .otp-box {
    width: 45px;
    height: 50px;
    text-align: center;
    font-size: 20px;
    border: 1px solid #ddd;
    border-radius: 6px;
    outline: none;
    transition: border-color 0.2s;
  }

  .otp-box:focus {
    border-color: #1a9b97;
    box-shadow: 0 0 0 2px rgba(26, 155, 151, 0.2);
  }

  .otp-footer {
    text-align: center;
    margin-top: 10px;
  }

  .resend-btn {
    border: none;
    background: none;
    color: #1a9b97;
    font-weight: 600;
    cursor: pointer;
  }

  .resend-btn:hover {
    text-decoration: underline;
  }

  .timer-text {
    color: #555;
    font-size: 14px;
  }
</style>
