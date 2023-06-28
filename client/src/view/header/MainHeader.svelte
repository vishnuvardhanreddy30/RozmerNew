<script>
    import { createEventDispatcher } from "svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import MobileSearch from "./MobileSearch.svelte";
    import Utils from "../../util/Utils";
    import Boot from "../../util/Boot";
    import Labels from "../../const/Labels";
    import Request from "../../util/Request";
    import SessionUtil from "../../util/SessionUtil";
    import urlConst from "../../const/Url";

    import logo from "../../assets/logo_white.png";

    const dispatch = createEventDispatcher();

    export let showLogout = false;

    export let showSearch = false;

    let showMobSearch = false;

    let searchVal;

    let searchIconText = "refresh";

    function onLogout() {
        Utils.confirm(
            Labels.dashboard.logout_msg,
            Labels.alert.logout,
            function (btn) {
                if (btn === "ok") {
                    Utils.mask(true);
                    let data = { email: SessionUtil.get("info", true).email };
                    Request.post(
                        urlConst.logout + Utils.encodeForUrl(data),
                        data,
                        (resp) => {
                            Utils.mask(true);
                            SessionUtil.removeAll();
                            Utils.reload();
                        },
                        (err) => {
                            Utils.mask();
                            Utils.log("[Logout] Failed to logout");
                        },
                        onLogout
                    );
                }
            }
        );
    }

    function onSearchFeed(e) {
        dispatch("searchpost", searchVal);
    }

    function showSearchField() {
        showMobSearch = !showMobSearch;
    }

    function onHideSearch() {
        showMobSearch = false;
    }

    function omMobileSearch(e) {
        dispatch("searchpost", e.detail);
    }

    function takeBacktoHome() {
        Utils.redirectTo('home');
    }

    $: {
        if (searchVal) {
            searchIconText = "search";
        } else {
            searchIconText = "refresh";
        }
    }
</script>

<Toolbar cls="theme-bg">
    <!-- <div slot="left" class="flex-cont">
        {#if !Boot.isBigScreen()}
            <div class="flex-cont">
                <Button iconCls="material-icons" iconText="menu" />
            </div>
        {/if}
    </div> -->
    <div slot="center">
        {#if showSearch && !Boot.isMobile()}
            <div class="flex-cont flex-vh search-cont">
                <TextField
                    placeholder={Labels.header.search}
                    cls="search-feed"
                    bind:value={searchVal}
                    on:enter={onSearchFeed}
                />
                <Button
                    iconCls="material-icons"
                    iconText={searchIconText}
                    on:click={onSearchFeed}
                />
            </div>
        {/if}
    </div>
    <div slot="left">
        <!-- <img loading="lazy" src="http://rozmer.co.in/wp-content/uploads/2022/02/Rosmer-logo-final-14.png" alt=""> -->
        <!-- <div class="title">{Labels.app.name}</div> -->
        <!-- <div class="flex-cont" /> -->
        <img
            src={logo}
            alt={Labels.app.name}
            class="header-logo flex-cont vh-center pl-1"
            on:click={takeBacktoHome}
        />
    </div>

    <div slot="right" class="flex-cont">
        {#if showSearch && Boot.isMobile()}
            <Button
                iconCls="material-icons"
                iconText="search"
                on:click={showSearchField}
            />
        {/if}
        {#if showLogout && !Boot.isBigScreen()}
            <!-- <Button
                iconCls="material-icons"
                iconText="publish"
                on:click={onLogout}
            /> -->
            <Button
                iconCls="material-icons"
                iconText="logout"
                on:click={onLogout}
            />
        {/if}
    </div>
</Toolbar>

{#if showSearch && showMobSearch && Boot.isMobile()}
    <MobileSearch
        on:hidesearch={onHideSearch}
        on:mobilesearch={omMobileSearch}
    />
{/if}

<style>
    :global(.search-cont .field-label) {
        display: none;
    }

    :global(.search-cont .btn-container) {
        padding: 0px;
        max-height: 36px;
    }

    :global(.search-cont .btn-container .btn-el) {
        padding: 4px 10px;
    }
</style>
