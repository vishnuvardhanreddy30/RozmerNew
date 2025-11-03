<script>
    import SessionUtil from "../../util/SessionUtil";
    import Utils from "../../util/Utils";
  
    let userInfo = {};
    $: userInfo = SessionUtil.get("info", true);
  
    // Form data
    let customer = {
      customer_email: "",
      customer_phone: "",
      customer_name: "",
      customer_bank_account_number: "",
      customer_bank_ifsc: "",
      customer_bank_code: "",
      customer_uid: ""
    };
  
    let orderCurrency = "";
    let orderAmount = "";
    let otp = "";
    let otpDigits = ["", "", "", "", "", ""];
    let showOtpSection = false;
    let otpVerified = false;
    let errors = {};
    let timer = 30;
    let canResend = false;
    let timerInterval;
  
    const currencyOptions = ["INR", "USD", "EUR"];
  
    // ✅ Validate form fields
    function validateForm() {
      errors = {};
      if (!customer.customer_email) errors.customer_email = "Please enter email.";
      if (!customer.customer_phone) errors.customer_phone = "Please enter phone number.";
      if (!customer.customer_name) errors.customer_name = "Please enter name.";
      if (!customer.customer_bank_account_number)
        errors.customer_bank_account_number = "Please enter account number.";
      if (!customer.customer_bank_ifsc) errors.customer_bank_ifsc = "Please enter IFSC.";
      if (!customer.customer_bank_code) errors.customer_bank_code = "Please enter bank code.";
      if (!customer.customer_uid) errors.customer_uid = "Please enter UID.";
      if (!orderCurrency) errors.orderCurrency = "Please select currency.";
      if (!orderAmount || Number(orderAmount) <= 0)
        errors.orderAmount = "Please enter valid amount.";
      if (showOtpSection && otpDigits.join("").length < 6)
        errors.otp = "Please enter 6-digit OTP.";
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
  
    // 🧾 Handle submit
    const handleSubmit = async () => {
      if (!validateForm()) return;
  
      if (!showOtpSection && !otpVerified) {
        try {
          const payload = {
            customer_details: {
              customer_email: customer.customer_email,
              customer_id: customer.customer_uid,
              customer_name: customer.customer_name,
              customer_phone: customer.customer_phone,
              customer_bank_account_number: customer.customer_bank_account_number,
              customer_bank_ifsc: customer.customer_bank_ifsc,
              customer_bank_code: Number(customer.customer_bank_code),
              customer_uid: customer.customer_uid
            },
            order_amount: Number(orderAmount),
            order_currency: orderCurrency,
            order_id: "test1234",
            order_meta: {
              notify_url: "https://google.com",
              payment_methods: "cc",
              return_url: "https://www.cashfree.com/devstudio/thankyou"
            },
            order_note: "string",
            userId: userInfo?.userId
          };
  
          const response = await fetch("http://localhost:9001/api/cashfree/create-order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
          });
  
          const result = await response.json();
          console.log("✅ Create Order API Response:", result);
  
          if (response.ok) {
            showOtpSection = true;
            alert("OTP sent to your registered email/phone!");
            startTimer();
          } else {
            alert("Failed to create order: " + (result.message || "Unknown error"));
          }
        } catch (err) {
          console.error(err);
          alert("Something went wrong while creating the order.");
        }
        return;
      }
  
      if (showOtpSection && !otpVerified) {
        otp = otpDigits.join("");
        if (otp === "123456") {
          otpVerified = true;
          alert("✅ OTP verified successfully!");
        } else {
          errors.otp = "Invalid OTP. Please try again.";
          return;
        }
      }
  
      if (otpVerified) {
        alert("🎉 Payment submitted successfully!");
        console.log("Final Payload:", {
          customer,
          order_currency: orderCurrency,
          order_amount: orderAmount,
          userId: userInfo?.userId
        });
      }
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
    <!-- 🧍 Customer Details Section -->
    {#if !showOtpSection}
    <fieldset class="space-y-4">
      <legend class="label-text">Customer Details</legend>
  
      <div class="form-grid">
        <div class="form-group">
          <label>Email *</label>
          <input type="email" bind:value={customer.customer_email} placeholder="Enter email" />
          {#if errors.customer_email}<span class="error-message">{errors.customer_email}</span>{/if}
        </div>
  
        <div class="form-group">
          <label>Phone *</label>
          <input type="text" bind:value={customer.customer_phone} placeholder="Enter phone" />
          {#if errors.customer_phone}<span class="error-message">{errors.customer_phone}</span>{/if}
        </div>
  
        <div class="form-group">
          <label>Name *</label>
          <input type="text" bind:value={customer.customer_name} placeholder="Enter name" />
          {#if errors.customer_name}<span class="error-message">{errors.customer_name}</span>{/if}
        </div>
  
        <div class="form-group">
          <label>Bank Account Number *</label>
          <input type="text" bind:value={customer.customer_bank_account_number} placeholder="Enter bank account number" />
          {#if errors.customer_bank_account_number}
            <span class="error-message">{errors.customer_bank_account_number}</span>
          {/if}
        </div>
  
        <div class="form-group">
          <label>IFSC Code *</label>
          <input type="text" bind:value={customer.customer_bank_ifsc} placeholder="Enter IFSC code" />
          {#if errors.customer_bank_ifsc}<span class="error-message">{errors.customer_bank_ifsc}</span>{/if}
        </div>
  
        <div class="form-group">
          <label>Bank Code *</label>
          <input type="number" bind:value={customer.customer_bank_code} placeholder="Enter bank code" />
          {#if errors.customer_bank_code}<span class="error-message">{errors.customer_bank_code}</span>{/if}
        </div>
  
        <div class="form-group">
          <label>UID *</label>
          <input type="text" bind:value={customer.customer_uid} placeholder="Enter UID" />
          {#if errors.customer_uid}<span class="error-message">{errors.customer_uid}</span>{/if}
        </div>
      </div>
    </fieldset>
  
    <!-- 💰 Payment Details Section -->
    <fieldset class="bg-gray-50 shadow-inner rounded-xl space-y-4 mt-4">
      <legend class="label-text">Payment Details</legend>
      <div class="form-grid">
        <div class="form-group">
          <label>Currency *</label>
          <select bind:value={orderCurrency}>
            <option value="">Select Currency</option>
            {#each currencyOptions as cur}
              <option value={cur}>{cur}</option>
            {/each}
          </select>
          {#if errors.orderCurrency}<span class="error-message">{errors.orderCurrency}</span>{/if}
        </div>
        <div class="form-group">
          <label>Amount *</label>
          <input type="number" bind:value={orderAmount} placeholder="Enter amount" />
          {#if errors.orderAmount}<span class="error-message">{errors.orderAmount}</span>{/if}
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
  
        {#if errors.otp}<span class="error-message">{errors.otp}</span>{/if}
  
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
  