<script>
    import Login from "./view/login/Login.svelte";
    import Main from "./view/dashboard/Main.svelte";
    import Register from "./view/register/Register.svelte";
    import ResetPassword from "./view/login/ResetPassword.svelte";
    import ResetPasswordConfirm from "./view/login/ResetPasswordConfirm.svelte";
    import Verify from "./view/register/Verify.svelte";
    import Loader from "./widget/Loader.svelte";
    import Utils from "./util/Utils";
    import { Routes } from "./store/Routes";
    import { LoadMask } from "./store/Loader";
    import Toast from "./widget/msg/Toast.svelte";

    let activeView, activeViewRef, routesData, showLoadMask;

    let viewMap = {
        login: Login,
        home: Main,
        register: Register,
        reset: ResetPassword,
        resetConfirm: ResetPasswordConfirm,
    };

    Routes.subscribe((val) => {
        if (location.search && location.search.indexOf("?code=") !== -1) {
            activeView = Verify;

            return;
        }

        if (location.search && location.search.indexOf("?token=") !== -1) {
            activeView = ResetPasswordConfirm;

            return;
        }

        routesData = val;
        if (
            activeViewRef &&
            activeViewRef.onRouteChange &&
            activeViewRef.onRouteChange(val) === false
        ) {
            return;
        }

        // if the active token is details view then activate the dashboard
        // and re-open the
        if (val && !activeViewRef && val.token === "details") {
            activeView = Main;

            setTimeout(() => {
                activeViewRef.showDetailsOnReload(val);
            }, 100);

            return;
        }

        if (!val || Utils.isEmpty(val.token)) {
            activeView = Login;

            return;
        }

        if (viewMap[val.token]) {
            activeView = viewMap[val.token];
        } else {
            activeView = Main;
        }
    });

    LoadMask.subscribe((value) => {
        showLoadMask = value;
    });

    function onChangeView(e) {
        Utils.redirectTo(e.detail);
    }

    function beforeUnload(e) {
        // debugger;
        // // if (dirty) {
        // e.preventDefault();
        // e.returnValue = "";
        // return "";
        // }
    }

    // Add orientation class to the body element
    function onViewResize() {
        document.body.classList.remove("x-landscape");
        document.body.classList.remove("x-portrait");
        document.body.classList.add(
            "x-" + (innerHeight > innerWidth ? "portrait" : "landscape")
        );
    }
    $: onViewResize();
</script>

<svelte:window on:beforeunload={beforeUnload} on:resize={onViewResize} />

<svelte:component
    this={activeView}
    on:changeview={onChangeView}
    bind:this={activeViewRef}
    {routesData}
/>
{#if showLoadMask}
    <Loader />
{/if}
<Toast />
