<script>
    import { createEventDispatcher } from 'svelte';
    let cardholderName = '';
    let cardNumber = '';
    let expiryDate = '';
    let cvv = '';
    let isProcessing = false;
    let errors = {};
    let dueAmount = 0; // Initialize dueAmount
    const dispatch = createEventDispatcher();

    // Fetch payment details from localStorage and calculate the total amount
    function calculateDueAmount() {
        const paymentDetails = JSON.parse(localStorage.getItem('paymentDetails') || '[]');
        dueAmount = paymentDetails.reduce((total, payment) => {
            return total + parseFloat(payment.amount || 0);
        }, 0);
    }

    // Format the due amount
    function formatAmount(amount) {
        return `₹${(amount / 100).toFixed(2)}`; // Convert paise to rupees and format
    }

    function validateForm() {
        errors = {};
        if (!cardholderName) errors.cardholderName = "Cardholder name is required.";
        if (!cardNumber) errors.cardNumber = "Card number is required.";
        if (!expiryDate) errors.expiryDate = "Expiry date is required.";
        if (!cvv) errors.cvv = "CVV is required.";
        return Object.keys(errors).length === 0;
    }

    function submitPayment(event) {
        event.preventDefault();
        if (!validateForm()) return;

        isProcessing = true;
        setTimeout(() => {
            alert('Payment submitted successfully!');
            resetForm();
            isProcessing = false;
        }, 2000);
    }

    function resetForm() {
        cardholderName = '';
        cardNumber = '';
        expiryDate = '';
        cvv = '';
        errors = {};
    }

    function viewHistory() {
        dispatch('viewHistory'); // Emit event to parent
    }

    // Calculate due amount when the script loads
    calculateDueAmount();

</script>

<div class="card-summary">
    <div class="amount-due">
        <h3>Due Amount</h3>
        <p>{formatAmount(dueAmount)}</p>
    </div>
    <button class="view-history-btn" on:click={viewHistory}>Details →</button>
</div>

<form class="payment-form" on:submit={submitPayment} novalidate>
    <fieldset>
        <legend>Enter Payment Details</legend>

        <div class="form-group">
            <label for="cardholderName">Cardholder Name</label>
            <input 
                id="cardholderName" 
                type="text" 
                bind:value={cardholderName} 
                placeholder="John Doe" 
                aria-required="true"
                aria-invalid={errors.cardholderName ? 'true' : 'false'}
            />
            {#if errors.cardholderName}
                <span class="error-message">{errors.cardholderName}</span>
            {/if}
        </div>

        <div class="form-group">
            <label for="cardNumber">Card Number</label>
            <input 
                id="cardNumber" 
                type="text" 
                bind:value={cardNumber} 
                placeholder="1234 5678 9012 3456" 
                maxlength="19" 
                aria-required="true"
                aria-invalid={errors.cardNumber ? 'true' : 'false'}
            />
            {#if errors.cardNumber}
                <span class="error-message">{errors.cardNumber}</span>
            {/if}
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="expiryDate">Expiry Date</label>
                <input 
                    id="expiryDate" 
                    type="text" 
                    bind:value={expiryDate} 
                    placeholder="MM/YY" 
                    maxlength="5" 
                    aria-required="true"
                    aria-invalid={errors.expiryDate ? 'true' : 'false'}
                />
                {#if errors.expiryDate}
                    <span class="error-message">{errors.expiryDate}</span>
                {/if}
            </div>

            <div class="form-group">
                <label for="cvv">CVV</label>
                <input 
                    id="cvv" 
                    type="password" 
                    bind:value={cvv} 
                    placeholder="123" 
                    maxlength="3" 
                    aria-required="true"
                    aria-invalid={errors.cvv ? 'true' : 'false'}
                />
                {#if errors.cvv}
                    <span class="error-message">{errors.cvv}</span>
                {/if}
            </div>
        </div>

        <button type="submit" disabled={isProcessing}>
            {isProcessing ? 'Processing...' : 'Submit Payment'}
        </button>
    </fieldset>
</form>

<style>
    .card-summary {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #f4f4f4;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 60%;
        margin: 20px auto;
        box-sizing: border-box;
    }

    .amount-due {
        text-align: left;
    }

    .amount-due h3 {
        margin: 0;
        font-size: 18px;
        color: #333;
    }

    .amount-due p {
        font-size: 24px;
        font-weight: bold;
        color: #1a9b97;
        margin: 5px 0 0 0;
    }

    .view-history-btn {
        background-color: #1a9b97;
        color: #fff;
        border: none;
        padding: 10px;
        width: 100px;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        font-weight: bold;
        text-align: center;
    }

    .view-history-btn:hover {
        background-color: #14877e;
    }

    .payment-form {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 60%;
        margin: 0 auto;
    }

    fieldset {
        border: none;
        padding: 0;
    }

    legend {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 16px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }

    label {
        font-weight: bold;
        margin-bottom: 5px;
    }

    input {
        padding: 10px;
        font-size: 16px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    input[aria-invalid="true"] {
        border-color: red;
    }

    .error-message {
        color: red;
        font-size: 14px;
        margin-top: 5px;
    }

    .form-row {
        display: flex;
        gap: 10px;
    }

    .form-row .form-group {
        flex: 1;
    }

    button {
        padding: 15px;
        font-size: 16px;
        color: white;
        background-color: #1a9b97;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        width: 100%;
        font-weight: bold;
    }

    button:disabled {
        background-color: #bbb;
    }

    button:hover:not(:disabled) {
        background-color: #14877e;
    }
</style>
