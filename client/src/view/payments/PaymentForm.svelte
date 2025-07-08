<script>
    import Base from '../../util/Base';
    import SessionUtil from '../../util/SessionUtil';
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Labels from "../../const/Labels";
    import Utils from "../../util/Utils";
    import { createEventDispatcher } from 'svelte';
    let cardholderName = '';
    let cardNumber = '';
    let expiryDate = '';
    let cvv = '';
    let isProcessing = false;
    let errors = {};
    let dueAmount = 0; // Initialize dueAmount
    let rechargeCoins = 0; // New field
    const dispatch = createEventDispatcher();
    let userInfo = {};

    $: {
        userInfo = SessionUtil.get("info", true);
    }

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

    function validateField(field, value) {
        switch (field) {
            case 'cardholderName':
                errors.cardholderName = value.trim() ? '' : "Cardholder name is required.";
                break;
            case 'cardNumber':
                const digitsOnly = value.replace(/\s/g, '');
                errors.cardNumber = digitsOnly.length === 16 ? '' : "Card number must be 16 digits.";
                break;
            case 'expiryDate':
                const match = value.match(/^(\d{2})\/(\d{2})$/);
                if (!match) {
                    errors.expiryDate = "Enter a valid date in MM/YY format.";
                } else {
                    const month = parseInt(match[1]);
                    const year = parseInt("20" + match[2]);
                    const now = new Date();
                    const expDate = new Date(year, month - 1);
                    if (month < 1 || month > 12) {
                        errors.expiryDate = "Invalid month.";
                    } else if (expDate < new Date(now.getFullYear(), now.getMonth())) {
                        errors.expiryDate = "Card is expired.";
                    } else {
                        errors.expiryDate = '';
                    }
                }
                break;
            case 'cvv':
                errors.cvv = /^\d{3}$/.test(value) ? '' : "CVV must be 3 digits.";
                break;
            case 'rechargeCoins':
                errors.rechargeCoins = !value || isNaN(value) || value < 0 ? "Recharge amount must be a valid number." : '';
                break;
        }
    }

    function validateForm() {
        validateField('cardholderName', cardholderName);
        validateField('cardNumber', cardNumber);
        validateField('expiryDate', expiryDate);
        validateField('cvv', cvv);
        validateField('rechargeCoins', rechargeCoins);

        return Object.values(errors).every(e => !e);
    }

    import axios from 'axios';

    function submitPayment(event) {
        event.preventDefault();
        if (!validateForm()) return;

        let obj = {
            userId: userInfo.userId,
            coinsToAdd: rechargeCoins,
            paymentReferenceId: "RAZORPAY_TXN_123456",
            cardHolderName: cardholderName,
            cardNumber: cardNumber.replace(/\s/g, ''),
            expiryDate: expiryDate,
            cvv: cvv
        };

        console.log("final form data:", obj);
        isProcessing = true;

        axios.post(
            urlConst.recharge_coins,
            {
                userId: obj.userId,
                coinsToAdd: obj.coinsToAdd,
                paymentReferenceId: obj.paymentReferenceId
            },
            {
                headers: Request.getHeaders(null),
                timeout: 120000
            }
        )
        .then(function (response) {
            console.log("response after payment:", response.data);
            resetForm();
            isProcessing = false;
            setTimeout(() => {
                dispatch('viewBalance');
            }, 1000)
        })
        .catch(function (err) {
            Utils.log(err);
            isProcessing = false;
        });
    }


    function resetForm() {
        cardholderName = '';
        cardNumber = '';
        expiryDate = '';
        cvv = '';
        rechargeCoins = 0;
        errors = {};
        // calculateDueAmount();
    }

    function viewHistory() {
        dispatch('viewHistory');
    }

    function formatCardNumber(event) {
        let input = event.target.value.replace(/\D/g, '').slice(0, 16);
        cardNumber = input.replace(/(.{4})/g, '$1 ').trim();
        validateField('cardNumber', cardNumber);
    }

    function handleExpiryInput(event) {
        let input = event.target.value.replace(/\D/g, '').slice(0, 4);
        if (input.length >= 3) {
            input = input.slice(0, 2) + '/' + input.slice(2);
        }
        expiryDate = input;
        validateField('expiryDate', expiryDate);
    }

    // Recalculate whenever rechargeCoins changes
    // $: calculateDueAmount();
</script>

<!-- <div class="card-summary">
    <div class="amount-due">
        <h3>Due Amount</h3>
        <p>{formatAmount(dueAmount)}</p>
    </div>
    <button class="view-history-btn" on:click={viewHistory}>Details →</button>
</div> -->

<form class="payment-form scrollable-form" on:submit={submitPayment} novalidate>
    <fieldset>
        <legend>Enter Payment Details</legend>

        <div class="form-group">
            <label for="rechargeCoins">Recharge Coins</label>
            <input 
                id="rechargeCoins" 
                type="number" 
                bind:value={rechargeCoins}
                min="0"
                placeholder="e.g. 50"
                aria-required="false"
                aria-invalid={errors.rechargeCoins ? 'true' : 'false'}
                on:input={(e) => validateField('rechargeCoins', e.target.value)}
            />
            {#if errors.rechargeCoins}
                <span class="error-message">{errors.rechargeCoins}</span>
            {/if}
        </div>

        <div class="form-group">
            <label for="cardholderName">Cardholder Name</label>
            <input 
                id="cardholderName" 
                type="text" 
                bind:value={cardholderName} 
                placeholder="John Doe" 
                aria-required="true"
                aria-invalid={errors.cardholderName ? 'true' : 'false'}
                on:input={(e) => validateField('cardholderName', e.target.value)}
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
                value={cardNumber}
                on:input={formatCardNumber}
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
                    value={expiryDate}
                    on:input={handleExpiryInput}
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
                    type="number" 
                    bind:value={cvv} 
                    placeholder="123" 
                    maxlength="3" 
                    on:input={(e) => validateField('cvv', e.target.value)}
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
        margin: 0 auto;
        max-height: 80vh;
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

    input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    @media (min-width: 768px) {
        .payment-form {
            width: 60%;
        }
    } 
</style>
