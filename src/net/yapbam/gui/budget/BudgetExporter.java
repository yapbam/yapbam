package net.yapbam.gui.budget;

import java.io.IOException;
import java.text.NumberFormat;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.export.ExportWriter;
import net.yapbam.export.Exporter;
import net.yapbam.gui.LocalizationData;

public class BudgetExporter extends Exporter<BudgetExporterParameters, BudgetView> {

	public BudgetExporter(BudgetExporterParameters parameters) {
		super(parameters);
	}

	@Override
	public void export(BudgetView budget, ExportWriter out) throws IOException {
		final boolean withSumColumn = getParameters().getCategorySumWording()!=null;
		out.addHeader();
		exportColumnNames(out, budget, withSumColumn);
		// Output category lines
		NumberFormat currencyFormatter = getParameters().getAmountFormat();
		for (int i=0;i<budget.getCategoriesSize();i++) {
			Category category = budget.getCategory(i);
			exportCategoryLine(out, currencyFormatter, budget, category, withSumColumn);
		}
		exportDateSumLine(out, currencyFormatter, budget, withSumColumn);
		out.addFooter();
	}

	private void exportColumnNames(ExportWriter out, BudgetView budget, final boolean withSumColumn)
			throws IOException {
		out.addLineStart();
		out.addValue(""); //$NON-NLS-1$
		for (int i = 0; i < budget.getDatesSize(); i++) {
			out.addValue(getParameters().getDateFormat().format(budget.getDate(i)));
		}
		if (withSumColumn) {
			out.addValue(getParameters().getCategorySumWording());
		}
		out.addLineEnd();
	}

	private void exportCategoryLine(ExportWriter out, NumberFormat currencyFormatter, BudgetView budget,
			Category category, final boolean withSumColumn) throws IOException {
		out.addLineStart();
		out.addValue(category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName()); //$NON-NLS-1$
		for (int j = 0; j < budget.getDatesSize(); j++) {
			Double value = budget.getAmount(budget.getDate(j), category);
			out.addValue(value!=null?currencyFormatter.format(value):""); //$NON-NLS-1$
		}
		if (withSumColumn) {
			double value = budget.getSum(category);
			out.addValue(currencyFormatter.format(value));
		}
		out.addLineEnd();
	}

	private void exportDateSumLine(ExportWriter out, NumberFormat currencyFormatter, BudgetView budget, final boolean withSumColumn) throws IOException {
		if (getParameters().getDateSumWording()!=null) {
			out.addLineStart();
			out.addValue(getParameters().getDateSumWording());
			for (int j = 0; j < budget.getDatesSize(); j++) {
				double value = budget.getSum(budget.getDate(j));
				out.addValue(currencyFormatter.format(value));
			}
			if (withSumColumn) {
				Double value = budget.getSum();
				out.addValue(value!=null?currencyFormatter.format(value):""); //$NON-NLS-1$
			}
			out.addLineEnd();
		}
	}
}
