<script>
    import { onMount } from "svelte";
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Request from "../../util/Request";
    import Utils from "../../util/Utils";
    import urlConst from "../../const/Url";
    import logo from "../../assets/logo.png";
    import Labels from "../../const/Labels";

    let verifictionText = "verified,";

    function onLogin() {
        location.search = "";
    }

    function onSuccess() {
        Utils.alert(Labels.register.user_verified, Labels.alert.register);
        Utils.mask();
    }

    function onFailure() {
        Utils.alert(Labels.register.verification_exp, Labels.alert.register);
        Utils.mask();
        verifictionText = Labels.register.verification_failed;
    }

    onMount(() => {
        Utils.mask(1);
        let url = urlConst.verify_user + location.search;

        Request.get(
            url,
            {
                ignoreCheck: true,
            },
            onSuccess,
            onFailure,
            onSuccess
        );
    });
</script>

<div
    class="main-sub-page wh-100-percent"
    transition:fly={{ x: window.innerWidth, duration: 500 }}
>
    <div class="wh-100-percent flex-1 flex-cont flex-vh">
        <div class="login-right flex-1 flex-cont flex-vh login-items">
            <div class="margin-bottom-to-child-15 p3">
                <img src={logo} alt="" class="sub-logo" />
                <div>
                    {Labels.register.user}
                    {verifictionText}
                    {Labels.register.continue}
                </div>
                <Toolbar ui="plain">
                    <div slot="center" class="flex-cont">
                        <Button text="Login" on:click={onLogin} />
                    </div>
                </Toolbar>
            </div>
        </div>
    </div>
</div>
