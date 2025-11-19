<script>
    import { createEventDispatcher, onMount } from 'svelte';
    import SessionUtil from '../../util/SessionUtil';
    import Utils from '../../util/Utils';
    import Request from '../../util/Request';
    import urlConst from '../../const/Url';
    import axios from 'axios';

    let currentCoins = 0;
    const dispatch = createEventDispatcher();

    let userInfo = SessionUtil.get("info", true) || {};

    function refreshCoins() {
        axios.get(urlConst.balance.replace("{loginUserId}", userInfo.userId), {
            headers: Request.getHeaders(null),
            params: null,
            timeout: 120000
        })
            .then(function (response) {
                console.log("response : ", response)
                currentCoins = response.data.walletBalance
            })
            .catch(function (err) {
                Utils.log(err);
            });
    }

    function viewPaymentForm() {
        dispatch('viewPaymentForm');
    }

    function viewPaymentHistory() {
        dispatch('viewPaymentHistory');
    }

    onMount(() => {
        refreshCoins();
    });
</script>

<div class="coin-balance-card">
    <div class="coin-info">
        <div class="icon">🪙</div>
        <div class="balance-info">
            <h2>Available Coins</h2>
            <div class="coin-count">
                <p>{currentCoins} Coins</p>
                <button class="refresh-btn" on:click={refreshCoins} title="Refresh coins">🔄</button>
            </div>
        </div>
    </div>
    <div class="button-container">
        <button class="view-history-btn" on:click={viewPaymentForm}>Recharge →</button>
        <button class="view-history-btn ml-3" on:click={viewPaymentHistory}>History →</button>
    </div>
</div>

<style>
    .coin-balance-card {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        border-radius: 16px;
        padding: 30px;
        width: 90%;
        max-width: 400px;
        margin: 40px auto;
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
        color: #333;
        min-height: 250px;
        /* background: linear-gradient(135deg, #f6d365 0%, #fda085 100%); */
    }

    .coin-info {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        text-align: center;
    }

    .icon {
        font-size: 48px;
        margin-bottom: 10px;
    }

    .balance-info h2 {
        margin: 0;
        font-size: 22px;
        color: #444;
    }

    .coin-count {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .balance-info p {
        font-size: 28px;
        font-weight: bold;
        margin: 5px 0 0;
    }

    .refresh-btn {
        background: none;
        border: none;
        font-size: 22px;
        cursor: pointer;
        color: #333;
        transition: transform 0.2s ease;
        outline: none;
    }

    .refresh-btn:hover {
        transform: rotate(90deg);
    }

    .button-container {
        display: flex;
        justify-content: center;
        margin-top: 20px;
    }

    .view-history-btn {
        background-color: #1a9b97;
        color: #fff;
        border: none;
        padding: 10px 20px;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        font-weight: bold;
        text-align: center;
    }

    .view-history-btn:hover {
        background-color: #14877e;
    }
</style>
