<script>
    import PaymentForm from './PaymentForm.svelte';
    import PaymentHistory from './PaymentHistory.svelte';
    import Balance from './Balance.svelte';
    import AddCustomerForm from './AddCustomerForm.svelte';
    import PaymentsFormLatest from './Recharge_Form.svelte';
    import Recharge from './Recharge.svelte';

    let activeTab = 'Add Bank details'; // Default tab
    // let tabs = [ 'Recharge', 'Payment History'];
    let tabs = [ 'Balance', 'Recharge', 'Payment History', 'Add Bank details'];

    // Switch tabs dynamically
    function switchTab(tabName) {
        activeTab = tabName;
    }

    // Handle the "viewHistory" event from PaymentForm
    function handleViewPaymentHistory() {
        activeTab = 'Payment History'; // Switch to the Payment History tab
    }

    function handleViewPaymentForm() {
        activeTab = 'Recharge'; // Switch to the Payment History tab
    }

    function handleViewBalance() {
        activeTab = 'Balance';
    }

    function handleViewAddCustomerForm() {
        activeTab = 'Add Bank details'
    }
</script>

<div class="payments-container">
    <div class="content-wrapper">
        <div class="tabs">
            {#each tabs as tab}
                <div 
                    class="tab {activeTab === tab ? 'active-tab' : ''}" 
                    on:click={() => switchTab(tab)}
                >
                    {tab}
                </div>
            {/each}
        </div>

        <div class="tab-content">
            {#if activeTab === 'Balance'}
                <Balance on:viewPaymentForm={handleViewPaymentForm} on:viewPaymentHistory={handleViewPaymentHistory}/>
            {:else if activeTab === 'Recharge'}
                <!-- <PaymentForm on:viewHistory={handleViewPaymentHistory} on:viewBalance={handleViewBalance}/> -->
                <!-- <PaymentsFormLatest on:viewAddCustomerForm={handleViewAddCustomerForm}/> -->
                <Recharge />
            {:else if activeTab === 'Payment History'}
                <PaymentHistory/>
            {:else if activeTab === 'Add Bank details'}
                <AddCustomerForm/>
            <!-- {:else if activeTab === 'Payments Form'}
                <PaymentsFormLatets on:viewAddCustomerForm={handleViewAddCustomerForm}/> -->
            {/if}

        </div>
    </div>
</div>

<style>
    .payments-container {
        width: 100%;
    }

    .content-wrapper {
        display: flex;
        flex-direction: row;
        margin-top: 1rem;
    }

    /* Sidebar tab styling for larger screens */
    .tabs {
        display: flex;
        flex-direction: column;
        width: 200px;
        border-right: 2px solid #ddd;
    }

    .tab {
        padding: 15px;
        cursor: pointer;
        border-bottom: 1px solid #ddd;
    }

    .tab.active-tab {
        font-weight: bold;
        color: #1a9b97;
    }

    .tab-content {
        flex: 1;
        padding: 15px;
    }

    /* Responsive design for smaller screens */
    @media (max-width: 768px) {
        .content-wrapper {
            flex-direction: column;
        }

        .tabs {
            flex-direction: row;
            width: 100%;
            border-right: none;
            border-bottom: 2px solid #ddd;
        }

        .tab {
            flex: 1;
            text-align: center;
            padding: 10px;
            border-bottom: none;
        }

        .tab.active-tab {
            border-bottom: 2px solid #1a9b97;
        }
    }

    :global(.profile-cont .field-container .field-label) {
        min-width: 100px;
    }
</style>
