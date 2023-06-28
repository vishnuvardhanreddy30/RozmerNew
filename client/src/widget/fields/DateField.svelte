<script context="module">
    let id = -1;
</script>

<script>
    export let label = "";
    export let name = "";
    export let required = false;
    export let labelSeperator = "";
    export let labelWidth = 100;
    export let value = new Date();

    let mm, dd, yy, df;

    let ignoreChange = false;

    let cmpId = "datefield-" + ++id;

    function parseDate(e){

        if(ignoreChange){
            return;
        }

        let elTarget = e && e.target;

        if(elTarget && elTarget.hasAttribute('ln') && 
        String(elTarget.value).length === 2 && 
        elTarget.nextElementSibling && isFinite(e.key)) {
            elTarget.nextElementSibling.focus()
        }

        let d = new Date(mm + '-' + dd + '-' + yy);

        if(isNaN(d.getDate())) {
            value = df = null;
            return;
        }

        let day = dd;
        let month = mm;

        if (day < 10) {
            day = '0' + day;
        }

        if (month < 10) {
            month = '0' + month;
        }

        value = df = (yy + '-' + month + '-' + day );
    }

    function onDfChange(e) {
        ignoreChange = true;

        let d = df.split('-');

        mm = d[1];
        dd = d[2];
        yy = d[0];

        ignoreChange = false;

        parseDate();
    }
</script>

<div class="field-container text-field">
    <label class="field-label" for={cmpId} width={labelWidth}
        >{label}
        {#if required}
            <span class="req-lbl">*</span>
        {/if}
        {labelSeperator}</label
    >

    <input style="display: none;" type="date" id={cmpId} {name} {required} bind:value />

    <div class="date-fld-cust flex">
        <input type="number" placeholder="DD" class="dfl1" bind:value={dd} on:keyup={parseDate} onKeyPress="if(this.value.length==2) return false;" ln />/
        <input type="number" placeholder="MM" class="dfl2" bind:value={mm} on:keyup={parseDate} onKeyPress="if(this.value.length==2) return false;" ln />/
        <input type="number" placeholder="YYYY" class="dfl3" bind:value={yy} on:keyup={parseDate} onKeyPress="if(this.value.length==4) return false;"/>
        <div style="flex:1;"></div>
        <input type="date" class="dfl4" bind:value={df} on:change={onDfChange}/>
    </div>
</div>

<style>
    .date-fld-cust {
        background: #fff;
        border: 2px solid #666;
        width: 210px;
    }
    .date-fld-cust input:focus {
    outline: none;
}

    .dfl1, .dfl2 {
        width: 25px;
    }

    .dfl3{
        width: 40px;
    }

    .dfl4{
        width: 18px;
    }

    .date-fld-cust input {
        border: 0;
    }

    	/* Chrome, Safari, Edge, Opera */
    .date-fld-cust input::-webkit-outer-spin-button,
    .date-fld-cust input::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    /* Firefox */
    .date-fld-cust input[type=number] {
        -moz-appearance: textfield;
    }
</style>
