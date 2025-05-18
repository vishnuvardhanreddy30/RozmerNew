<script>
    import { onMount } from 'svelte';
    import urlConst from "../../const/Url";
    import SessionUtil from '../../util/SessionUtil';
  
    let transactions = [];
    let loading = false;
    let selectedFilter = 'all';
    let userInfo = {};
  
    $: {
        userInfo = SessionUtil.get("info", true);
    }
  
    const fetchTransactions = async () => {
      loading = true;
      let url = `${urlConst.transaction_history}?userId=${userInfo.userId}`;
      
      if (selectedFilter === 'recharge') {
        url += '&transactionType=recharge';
      } else if (selectedFilter === 'unlock') {
        url += '&transactionType=unlock';
      }
  
      try {
        const res = await fetch(url);
        const data = await res.json();
        transactions = data.transactions || [];
        console.log("response : ", res , data)
      } catch (error) {
        console.error('Failed to fetch transactions:', error);
        transactions = [];
      } finally {
        loading = false;
      }
    };
  
    // Fetch on component mount
    onMount(() => {
      fetchTransactions();
    });
  
    // React to filter change
    $: if (selectedFilter !== undefined) {
      fetchTransactions();
    }
  </script>

<div class="payment-history">
    <div class="Payment-header">
        <h2>Payment History</h2>
        <select bind:value={selectedFilter} class="payment-filter">
            <option value="all">All</option>
            <option value="recharge">Recharge</option>
            <option value="unlock">Unlock</option>
          </select>
    </div>

    {#if loading}
        <p class="loading">Loading transactions...</p>
    {:else if transactions.length === 0}
        <p class="no-data">No transactions found.</p>
    {:else}
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Coins</th>
                <th>Description</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            {#each transactions as txn}
                <tr>
                <td>{txn.transactionId}</td>
                <td>{txn.type}</td>
                <td>{txn.coins}</td>
                <td>{txn.description}</td>
                <td>{new Date(txn.date).toLocaleString()}</td>
                </tr>
            {/each}
            </tbody>
        </table>
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
        min-height: 400px;
        max-height: 66vh;
    }
    .no-data, .loading{
        font-weight: bold;
        margin-top: 5rem;
    }
    .Payment-header{
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
    }
    .payment-filter{
        width: 120px;
        padding: 0 5px;
        border: 1px solid black;
        border-radius: 10px;
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
