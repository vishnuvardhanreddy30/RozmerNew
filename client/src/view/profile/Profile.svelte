<script>
    import UpdateDetails from "./UpdateDetails.svelte";
    import MyPosts from "./MyPosts.svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import SessionUtil from "../../util/SessionUtil";
    import Labels from "../../const/Labels";

    let userInfo = {};
    let activeView = UpdateDetails;
    let getPost = true;

    $: {
        userInfo = SessionUtil.get("info", true);
    }
</script>

<div class="profile-cont wh-100-percent">
    <Toolbar ui="plain">
        <div slot="center">
            Welcome
            <b
                >{userInfo.firstName}
                {userInfo.lastName}</b
            >
        </div>
        <!-- {#if getPost} 
            <Button slot="right" text="Get My Posts" on:click={getAllPost} />
        {/if} -->
        <!-- <Button slot="right" text="Get My Posts" on:click={getAllPost} /> -->
    </Toolbar>

    <svelte:component this={activeView} />
</div>

<style>
    :global(.profile-cont .field-container .field-label) {
        min-width: 100px;
    }
</style>
