package net.yapbam.gui.budget;

import java.io.IOException;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.export.ExportWriter;
import net.yapbam.export.Exporter;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.export.ExporterParameters;

public class BudgetExporter extends Exporter<ExporterParameters<BudgetExporterExtraData>, BudgetView> {

	public BudgetExporter(ExporterParameters<BudgetExporterExtraData> parameters) {
		super(parameters);
	}

	@Override
	public void export(BudgetView budget, ExportWriter out) throws IOException {
		final boolean withSumColumn = getParameters().getDataExtension().getCategorySumWording()!=null;
		out.addHeader();
		exportColumnNames(out, budget, withSumColumn);
		// Output category lines
		for (int i=0;i<budget.getCategoriesSize();i++) {
			Category category = budget.getCategory(i);
			exportCategoryLine(out, budget, category, withSumColumn);
		}
		exportDateSumLine(out, budget, withSumColumn);
		out.addFooter();
	}

	private void exportColumnNames(ExportWriter out, BudgetView budget, final boolean withSumColumn)
			throws IOException {
		out.addLineStart();
		out.addValue(""); //$NON-NLS-1$
		for (int i = 0; i < budget.getDatesSize(); i++) {
			out.addValue(getParameters().format(budget.getDate(i)));
		}
		if (withSumColumn) {
			out.addValue(getParameters().getDataExtension().getCategorySumWording());
		}
		out.addLineEnd();
	}

	private void exportCategoryLine(ExportWriter out, BudgetView budget, Category category, final boolean withSumColumn) throws IOException {
		out.addLineStart();
		out.addValue(category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName()); //$NON-NLS-1$
		for (int j = 0; j < budget.getDatesSize(); j++) {
			Double value = budget.getAmount(budget.getDate(j), category);
			out.addValue(value!=null?getParameters().format(value):""); //$NON-NLS-1$
		}
		if (withSumColumn) {
			double value = budget.getSum(category);
			out.addValue(getParameters().format(value));
		}
		out.addLineEnd();
	}

	private void exportDateSumLine(ExportWriter out, BudgetView budget, final boolean withSumColumn) throws IOException {
		if (getParameters().getDataExtension().getDateSumWording()!=null) {
			out.addLineStart();
			out.addValue(getParameters().getDataExtension().getDateSumWording());
			for (int j = 0; j < budget.getDatesSize(); j++) {
				double value = budget.getSum(budget.getDate(j));
				out.addValue(getParameters().format(value));
			}
			if (withSumColumn) {
				Double value = budget.getSum();
				out.addValue(value!=null?getParameters().format(value):""); //$NON-NLS-1$
			}
			out.addLineEnd();
		}
	}
}
