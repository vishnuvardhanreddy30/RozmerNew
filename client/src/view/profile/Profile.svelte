<script>
    import UpdateDetails from "./UpdateDetails.svelte";
    import MyPosts from "./MyPosts.svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import SessionUtil from "../../util/SessionUtil";
    import MyComments from "./MyComments.svelte";

    let userInfo = {};
    let selectedUserId = null;
    let activeTab = 'Profile Details';
    let activeView = UpdateDetails;

    $: {
        userInfo = SessionUtil.get("info", true);
    }

    // Function to switch tabs
    function switchTab(tabName) {
        activeTab = tabName;
        if(tabName === 'Profile Details') {
            activeView = UpdateDetails
        } else if(tabName === 'Posts') {
            // @ts-ignore
            activeView = MyPosts
        } else {
            // @ts-ignore
            activeView = MyComments
        }
        // activeView = tabName === 'Profile Details' ? UpdateDetails : MyPosts;
    }
    $: {
        selectedUserId = getUIDFromHash()
        
    }
    function getUIDFromHash() {
        const hash = location.hash; // Get the hash part of the URL
        const queryString = hash.includes('?') ? hash.split('?')[1] : '';
        const params = new URLSearchParams(queryString);
        return params.get('uid');
    }
</script>

<div class="profile-container">
    <Toolbar ui="plain">
        <div slot="center">
            Welcome <b>{userInfo.firstName} {userInfo.lastName}</b>
        </div>
    </Toolbar>

    <div class="content-wrapper">
        <div class="tabs">
            <div
                class="tab"
                class:active-tab={activeTab === 'Profile Details'}
                on:click={() => switchTab('Profile Details')}
            >
                Profile Details
            </div>
            <div
                class="tab"
                class:active-tab={activeTab === 'Posts'}
                on:click={() => switchTab('Posts')}
            >
                Posts
            </div>
            {#if selectedUserId == null || userInfo.userId == selectedUserId }
            <div
                class="tab"
                class:active-tab={activeTab === 'Comments'}
                on:click={() => switchTab('Comments')}
            >
                Comments
            </div>
            {/if}
        </div>

        <div class="tab-content">
            <svelte:component this={activeView} />
        </div>
    </div>
</div>

<style>
    .profile-container {
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
        background-color: #f0f0f0;
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
