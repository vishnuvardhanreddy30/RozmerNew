<script>
    // Fetch payment details from localStorage
    let paymentHistory = JSON.parse(localStorage.getItem("paymentDetails")) || [];

    // Convert amounts from paise to rupees and format the date
    paymentHistory = paymentHistory.map((payment) => ({
        ...payment,
        amount: `₹${(Number(payment.amount) / 100).toFixed(2)}`, // Convert to rupees and format to 2 decimal places
        formattedDate: formatDate(payment.date) // Format the date
    }));

    // Function to format the date
    function formatDate(dateString) {
        const date = new Date(dateString);
        const options = {
            day: "numeric",
            month: "short",
            year: "numeric",
            hour: "numeric",
            minute: "2-digit",
            hour12: true,
        };
        // @ts-ignore
        return date.toLocaleDateString("en-US", options).replace(',', '').replace('at', '');
    }
</script>

<div class="payment-history">
    <h2>Payment History</h2>

    {#if paymentHistory.length > 0}
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Activity Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                </tr>
            </thead>
            <tbody>
                {#each paymentHistory as payment}
                    <tr>
                        <td>{payment.title}</td>
                        <td>{payment.formattedDate}</td>
                        <td>{payment.type}</td>
                        <td>{payment.amount}</td>
                    </tr>
                {/each}
            </tbody>
        </table>
    {:else}
        <p>No payment history found.</p>
    {/if}
</div>

<style>
    .payment-history {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 90%;
        margin: 20px auto;
    }

    h2 {
        margin-bottom: 20px;
        text-align: center;
        font-size: 24px;
        color: #333;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 16px;
    }

    th, td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }

    th {
        background-color: #f4f4f4;
    }

    tr:hover {
        background-color: #f9f9f9;
    }

    p {
        text-align: center;
        font-size: 18px;
        color: #777;
    }
</style>
