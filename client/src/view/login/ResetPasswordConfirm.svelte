<script>
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import PasswordField from "../../widget/fields/PasswordField.svelte";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Utils from "../../util/Utils";

    import logo from "../../assets/logo.png";
    import Labels from "../../const/Labels";

    let required = true,
        pass,
        cnfPass;

    function onBack() {
        Utils.reload();
    }

    function onSuccess(res) {
        Utils.alert(Labels.reset.confirm, Labels.alert.reset_pass, onBack);

        Utils.mask();
        Utils.log(res);
    }

    function onFailure(err) {
        Utils.mask();

        Utils.log(err);
        Utils.alert(
            Labels.reset.token_expired,
            Labels.alert.reset_pass,
            onBack
        );
    }
    function onSubmit() {
        if (Utils.isEmpty(pass) || Utils.isEmpty(cnfPass)) {
            Utils.alert(Labels.reset.validation_msg, Labels.alert.reset_pass);
            return;
        }
        Utils.mask(true);

        let data = {
            password: pass,
            token: Utils.getParamsAsObject(location.search).token,
            ignoreCheck: true,
        };

        Request.post(
            urlConst.reset_pass + Utils.encodeForUrl(data),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
    }
</script>

<div
    class="main-sub-page wh-100-percent"
    transition:fly={{ x: window.innerWidth, duration: 500 }}
>
    <div class="wh-100-percent flex-1 flex-cont flex-vh">
        <div class="flex-1 wh-100-percent bg-gradient login-left">
            <!-- <div class="login-title-cont">
                {Labels.reset.flash_msg_confirm}
            </div> -->
        </div>
        <div class="login-right flex-1 flex-cont flex-vh">
            <div class="margin-bottom-to-child-15 p3 login-items">
                <img src={logo} alt="" class="sub-logo" />
                <div class="flex-cont flex-vh wh-100-percent flex-dir-column">
                    <div class="margin-bottom-to-child-15 p3">
                        <PasswordField
                            {required}
                            label={Labels.reset.new_pass}
                            bind:value={pass}
                        />

                        <PasswordField
                            {required}
                            label={Labels.reset.cnf_pass}
                            bind:value={cnfPass}
                        />
                    </div>
                </div>

                <Toolbar ui="plain">
                    <div slot="center" class="flex-cont">
                        <Button text="Back" on:click={onBack} />
                        <Button text="Submit" on:click={onSubmit} />
                    </div>
                </Toolbar>
            </div>
        </div>
    </div>
</div>
