<script>
    import { onMount } from "svelte";
    import MainHeader from "../header/MainHeader.svelte";
    import FeedList from "../feed/FeedList.svelte";
    // import MobileFeedList from "../feed/MobileFeedList.svelte";
    import SideMenu from "./SideMenu.svelte";
    import Notification from "../notification/Notification.svelte";
    import Publish from "../publish/Publish.svelte";
    import MobilePublish from "../publish/MobilePublish.svelte";
    import Profile from "../profile/Profile.svelte";
    import MobileMenu from "./MobileMenu.svelte";
    import Collaborate from "../publish/Collaborate.svelte";
    import MyPosts from "../profile/MyPosts.svelte";
    import Boot from "../../util/Boot";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    import SessionUtil from "../../util/SessionUtil";

    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";

    export let routesData;

    let wrapperEl, activeViewRef;
    let isMobile = Boot.isMobile();
    let activeView = FeedList; //isMobile ? MobileFeedList : FeedList;

    let showSideMenu = false;

    let selected;
    let showSearch = false;

    let viewMap = {
        home: FeedList, //isMobile ? MobileFeedList : FeedList,
        notifications: Notification,
        publish: isMobile ? MobilePublish : Publish,
        account: Profile,
        collaborate: Collaborate,
        mypost: MyPosts
    };

    export function onRouteChange(data) {
        // return false;

        if(activeViewRef.onRouteChange) {
            activeViewRef.onRouteChange(data);
        }
    }

    export function showDetailsOnReload(val) {
        activeViewRef.onItemClick && activeViewRef.onItemClick(null, val);
    }

    let showLogout = true;

    function onViewResize() {
        // var mainHeaderBox =
        //     wrapperEl.previousElementSibling.getBoundingClientRect(),
        //     mobileNavHeight = 0,
        //     mobileNav;
        // if(!Boot.isDesktop() && wrapperEl.children[1]) {
        //     mobileNav = wrapperEl.children[1].getBoundingClientRect();
        //     mobileNavHeight = mobileNav.height;
        // }
        // wrapperEl.style.height =
        //     window.innerHeight - (mainHeaderBox.height + mobileNavHeight) + "px";

        if (activeViewRef && activeViewRef.handleResize) {
            activeViewRef.handleResize();
        }
    }

    function onActivateView(e) {
        let viewMapRef = e.detail.view;

        if (!Utils.isValidUser()) {
            return Utils.reload();
        }

        if (viewMapRef === "logout") {
            return onLogout();
        }
        // if any popup is opened close it
        Utils.hideAlert();

        let view = viewMap[viewMapRef];

        if (view) {
            activeView = view;
            location.hash = viewMapRef;

            setTimeout(() => {
                if (activeViewRef && activeViewRef.handleResize) {
                    activeViewRef.handleResize();
                }
            }, 200);
        }
    }

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

    function onSearchPost(e) {
        if (activeViewRef.onSearch) {
            activeViewRef.onSearch(e.detail);
        }
    }

    onMount(() => {
        onViewResize();

        let activatedHash = SessionUtil.get('activatedHash');

        if(!Utils.isEmpty(activatedHash) && activatedHash !== 'null') {
            window.location.hash = activatedHash;

            SessionUtil.set('activatedHash', null);
        }
    });

    $: {
        if (!Utils.isValidUser()) {
            Utils.reload();
        }

        // show side menu only in desktop, exclude Tablet and mobile
        if (Boot.isDesktop()) {
            showSideMenu = true;
        }

        if (!Utils.isEmpty(routesData) && viewMap[routesData.token]) {
            activeView = viewMap[routesData.token];

            // if any popup is opened close it
            Utils.hideAlert();
        }

        selected =
            (routesData && routesData.token) || location.hash.replace("#", "");

        showSearch = selected === "home";
    }
</script>

<svelte:window on:resize={onViewResize} />
<MainHeader {showLogout} {showSearch} on:searchpost={onSearchPost}/>

<div
    class="flex-cont wh-100-percent"
    bind:this={wrapperEl}
    class:flex-dir-column={!Boot.isDesktop()}
    class:flex-mobile={!Boot.isDesktop()}
>
    {#if showSideMenu}
        <SideMenu on:showview={onActivateView} {selected} />
    {/if}

    <svelte:component this={activeView} bind:this={activeViewRef} />
</div>
{#if !Boot.isDesktop()}
    <MobileMenu on:showview={onActivateView} {selected} />
{/if}
