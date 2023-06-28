<script>
    import Utils from "../../util/Utils";

    export let columns;
    export let checkColumn = false;
    export let data;
    export let rowTpl;
    export let showRowNumber = false;
    export let hideHeader = false;
    export let checkRadio = false;

    let allSelected = false;
    let radioSelectedRow;

    $: if (checkColumn && !Utils.isEmpty(data)) {
        data = data.map((item) => {
            item.checked = item.checked || false;

            return item;
        });

        allSelected =
            data.filter((item) => item.checked).length === data.length;
    }
    $: colIndex = columns.map((item) => item.dataIndex);

    function onSelectionChange(e) {
        let checked = false;

        if (Utils.isEmpty(data)) {
            return;
        }

        if (e.target.checked) {
            checked = true;
        }
        data = data.map((item) => {
            item.checked = checked;

            return item;
        });
    }

    export function getSelectedRecord() {
        if (Utils.isEmpty(data)) {
            return;
        }

        if (checkRadio) {
            if (Utils.isEmpty(radioSelectedRow)) {
                return [];
            }

            return [radioSelectedRow];
        }

        return data.filter((item) => item.checked);
    }

    function onRadioCheck(e) {
        var idx = e.target.getAttribute("row-index");

        radioSelectedRow = data[idx];
    }
</script>

<table>
    <thead class:hidden={hideHeader}>
        <tr>
            {#if columns}
                {#if checkColumn}
                    <th width="50"
                        ><input
                            type="checkbox"
                            bind:checked={allSelected}
                            on:click={onSelectionChange}
                        />
                    </th>
                {/if}
                {#if checkRadio}
                    <th width="50"><input type="radio" /> </th>
                {/if}
                {#if showRowNumber}
                    <th width="50">S.No. </th>
                {/if}
                {#each columns as column}
                    <th>{column.text}</th>
                {/each}
            {/if}
        </tr>
    </thead>

    <!-- body section -->
    {#if data}
        {#each data as item, rowIdx}
            <tr>
                {#if checkColumn}
                    <td
                        ><input
                            type="checkbox"
                            bind:checked={item["checked"]}
                        /></td
                    >
                {/if}
                {#if checkRadio}
                    <td
                        ><input
                            name="radio-check-column"
                            type="radio"
                            row-index={rowIdx}
                            on:click={onRadioCheck}
                        /></td
                    >
                {/if}
                {#if showRowNumber}
                    <td>{rowIdx + 1}</td>
                {/if}
                {#each colIndex as idx}
                    {#if rowTpl}
                        <td>
                            {@html rowTpl(item)}
                        </td>
                    {:else}
                        <td>
                            {item[idx]}
                        </td>
                    {/if}
                {/each}
            </tr>
        {/each}
    {:else}
        <tr>
            <td colspan={colIndex && colIndex.length + 1} align="center"
                ><div class="b text-center">No record</div></td
            >
        </tr>
    {/if}
</table>

<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    td,
    th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
        font-size: 14px;
    }

    td input,
    th input {
        height: 16px;
        width: 16px;
    }

    tr:nth-child(even) {
        background-color: #f8f8f8;
    }
</style>
