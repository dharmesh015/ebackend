package com.ecom.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class OrderBillPDFGenerator {
    // Define reusable colors
    private static final BaseColor HEADER_COLOR = new BaseColor(34, 139, 34); // Forest Green for header
    private static final BaseColor ACCENT_COLOR = new BaseColor(128, 0, 0); // Maroon for accent
    private static final BaseColor LIGHT_GRAY = new BaseColor(240, 240, 240);
    private static final BaseColor TABLE_HEADER_COLOR = new BaseColor(220, 220, 220);

    public static byte[] generateOrderBillPDF(OrderDetail orderDetail) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Define fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, HEADER_COLOR);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, HEADER_COLOR);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            Font accentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, ACCENT_COLOR);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            PdfContentByte cb = writer.getDirectContent();

            // Create store logo and header
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[] { 1, 3 });

            // Logo cell (you would replace this with your actual logo)
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setPadding(10);

            // Create a placeholder for the logo
            Barcode128 code128 = new Barcode128();
            code128.setCode("EC" + generateOrderNumber(orderDetail));
            code128.setCodeType(Barcode128.CODE128);
            Image logoImage = code128.createImageWithBarcode(cb, null, null);
            logoImage.scaleToFit(80, 80);
            logoCell.addElement(logoImage);
            headerTable.addCell(logoCell);

            // Title cell
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setPadding(10);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph title = new Paragraph("Quick Cart", titleFont);
            Paragraph subtitle = new Paragraph("ORDER RECEIPT", subtitleFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            subtitle.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(title);
            titleCell.addElement(subtitle);
            headerTable.addCell(titleCell);

            document.add(headerTable);

            // Add a colored separator line
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(HEADER_COLOR);
            ls.setLineWidth(2);
            document.add(new Chunk(ls));

            document.add(new Paragraph(" ")); // Space

            // Order info section
            PdfPTable orderInfoTable = new PdfPTable(3);
            orderInfoTable.setWidthPercentage(100);
            orderInfoTable.setWidths(new float[] { 1, 1, 1 });

            // Order number
            PdfPCell orderNumberCell = createLabelValueCell("ORDER #", generateOrderNumber(orderDetail),
                    smallFont, boldFont);
            orderInfoTable.addCell(orderNumberCell);

            // Order date
            Date orderDate = new Date(); // Use current date for this example
            String formattedOrderDate;
            try {
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                formattedOrderDate = outputFormat.format(orderDate);
            } catch (Exception e) {
                // Fallback in case of formatting error
                formattedOrderDate = orderDate.toString();
            }

            PdfPCell dateCell = createLabelValueCell("DATE", formattedOrderDate, smallFont, boldFont);
            orderInfoTable.addCell(dateCell);

            // Order status
            PdfPCell statusCell = createLabelValueCell("STATUS", orderDetail.getOrderStatus(), smallFont, boldFont);
            orderInfoTable.addCell(statusCell);

            document.add(orderInfoTable);
            document.add(new Paragraph(" ")); // Space

            // Customer Information
            PdfPTable customerTable = new PdfPTable(1);
            customerTable.setWidthPercentage(100);

            PdfPCell customerHeaderCell = new PdfPCell(new Paragraph("CUSTOMER INFORMATION", subtitleFont));
            customerHeaderCell.setBorder(Rectangle.NO_BORDER);
            customerHeaderCell.setPaddingBottom(5);
            customerTable.addCell(customerHeaderCell);

            PdfPTable customerDetailsTable = new PdfPTable(2);
            customerDetailsTable.setWidthPercentage(100);

            // Customer name
            PdfPCell nameCell = createLabelValueCell("Name", orderDetail.getOrderFullName(), smallFont, boldFont);
            customerDetailsTable.addCell(nameCell);

            // Customer contact
            PdfPCell contactCell = createLabelValueCell("Contact", orderDetail.getOrderContactNumber(), smallFont, boldFont);
            customerDetailsTable.addCell(contactCell);

            

            // Alternate contact
            if (orderDetail.getOrderAlternateContactNumber() != null && !orderDetail.getOrderAlternateContactNumber().isEmpty()) {
                PdfPCell altContactCell = createLabelValueCell("Alternate Contact", 
                    orderDetail.getOrderAlternateContactNumber(), smallFont, regularFont);
                altContactCell.setColspan(2);
                customerDetailsTable.addCell(altContactCell);
            }

            document.add(customerTable);
            document.add(customerDetailsTable);
            document.add(new Paragraph(" ")); // Space

            // Order Items Section
            PdfPTable orderItemsHeaderTable = new PdfPTable(1);
            orderItemsHeaderTable.setWidthPercentage(100);

            PdfPCell itemsHeaderCell = new PdfPCell(new Paragraph("ORDER ITEMS", subtitleFont));
            itemsHeaderCell.setBorder(Rectangle.NO_BORDER);
            itemsHeaderCell.setPaddingBottom(5);
            orderItemsHeaderTable.addCell(itemsHeaderCell);
            document.add(orderItemsHeaderTable);

            // Products table
            PdfPTable productsTable = new PdfPTable(4);
            productsTable.setWidthPercentage(100);
            productsTable.setWidths(new float[] { 4, 1, 2, 2 });

            // Table header
            PdfPCell productHeaderCell = new PdfPCell(new Paragraph("Product", tableHeaderFont));
            productHeaderCell.setBackgroundColor(TABLE_HEADER_COLOR);
            productHeaderCell.setPadding(5);
            productsTable.addCell(productHeaderCell);

            PdfPCell qtyHeaderCell = new PdfPCell(new Paragraph("Qty", tableHeaderFont));
            qtyHeaderCell.setBackgroundColor(TABLE_HEADER_COLOR);
            qtyHeaderCell.setPadding(5);
            qtyHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            productsTable.addCell(qtyHeaderCell);

            PdfPCell priceHeaderCell = new PdfPCell(new Paragraph("Price", tableHeaderFont));
            priceHeaderCell.setBackgroundColor(TABLE_HEADER_COLOR);
            priceHeaderCell.setPadding(5);
            priceHeaderCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            productsTable.addCell(priceHeaderCell);

            PdfPCell totalHeaderCell = new PdfPCell(new Paragraph("Total", tableHeaderFont));
            totalHeaderCell.setBackgroundColor(TABLE_HEADER_COLOR);
            totalHeaderCell.setPadding(5);
            totalHeaderCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            productsTable.addCell(totalHeaderCell);

            // For the example, we just add the single product from the order
            Product product = orderDetail.getProduct();
            
            // Calculate quantity based on total amount and product price
            int quantity = (int) (orderDetail.getOrderAmount() / product.getProductDiscountedPrice());
            
            // Product information
            PdfPCell productCell = new PdfPCell(new Paragraph(product.getProductName(), regularFont));
            productCell.setPadding(5);
            productsTable.addCell(productCell);

            // Quantity
            PdfPCell qtyCell = new PdfPCell(new Paragraph(String.valueOf(quantity), regularFont));
            qtyCell.setPadding(5);
            qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            productsTable.addCell(qtyCell);

            // Unit price
            PdfPCell priceCell = new PdfPCell(new Paragraph(
                    String.format("$%.2f", product.getProductDiscountedPrice()), regularFont));
            priceCell.setPadding(5);
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            productsTable.addCell(priceCell);

            // Total price for the item
            PdfPCell totalCell = new PdfPCell(new Paragraph(
                    String.format("$%.2f", orderDetail.getOrderAmount()), boldFont));
            totalCell.setPadding(5);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            productsTable.addCell(totalCell);

            document.add(productsTable);

            // Order summary (totals)
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(40);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.setSpacingBefore(10);

            // Subtotal row
            PdfPCell subtotalLabelCell = new PdfPCell(new Paragraph("Subtotal:", regularFont));
            subtotalLabelCell.setBorder(Rectangle.NO_BORDER);
            subtotalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalsTable.addCell(subtotalLabelCell);

            PdfPCell subtotalValueCell = new PdfPCell(new Paragraph(
                    String.format("$%.2f", orderDetail.getOrderAmount()), regularFont));
            subtotalValueCell.setBorder(Rectangle.NO_BORDER);
            subtotalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.addCell(subtotalValueCell);

            // Shipping row
            PdfPCell shippingLabelCell = new PdfPCell(new Paragraph("Shipping:", regularFont));
            shippingLabelCell.setBorder(Rectangle.NO_BORDER);
            shippingLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalsTable.addCell(shippingLabelCell);

            // For the example, we'll use free shipping
            PdfPCell shippingValueCell = new PdfPCell(new Paragraph("Free", regularFont));
            shippingValueCell.setBorder(Rectangle.NO_BORDER);
            shippingValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.addCell(shippingValueCell);

            // Tax row
            PdfPCell taxLabelCell = new PdfPCell(new Paragraph("Tax:", regularFont));
            taxLabelCell.setBorder(Rectangle.NO_BORDER);
            taxLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalsTable.addCell(taxLabelCell);

            // For the example, we'll say tax is included
            PdfPCell taxValueCell = new PdfPCell(new Paragraph("Included", regularFont));
            taxValueCell.setBorder(Rectangle.NO_BORDER);
            taxValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.addCell(taxValueCell);

            // Add a separator line before total
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setColspan(2);
            separatorCell.setBorder(Rectangle.TOP);
            separatorCell.setBorderColor(BaseColor.GRAY);
            separatorCell.setPaddingTop(2);
            totalsTable.addCell(separatorCell);

            // Total row
            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("Total:", boldFont));
            totalLabelCell.setBorder(Rectangle.NO_BORDER);
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalsTable.addCell(totalLabelCell);

            PdfPCell totalValueCell = new PdfPCell(new Paragraph(
                    String.format("$%.2f", orderDetail.getOrderAmount()), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, ACCENT_COLOR)));
            totalValueCell.setBorder(Rectangle.NO_BORDER);
            totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.addCell(totalValueCell);

            document.add(totalsTable);

            // Barcode at bottom
            document.add(new Paragraph(" ")); // Space

            Barcode128 barcode = new Barcode128();
            barcode.setCode("EC" + generateOrderNumber(orderDetail));
            barcode.setCodeType(Barcode128.CODE128);
            Image barcodeImage = barcode.createImageWithBarcode(cb, null, null);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            barcodeImage.scaleToFit(300, 50);
            document.add(barcodeImage);

            // Footer with terms
            document.add(new Paragraph(" ")); // Space
            Paragraph terms = new Paragraph(
                    "Thank you for your order! For returns and exchanges, please contact customer service within 30 days of purchase. "
                            + "Keep this receipt for your records.",
                    smallFont);
            terms.setAlignment(Element.ALIGN_CENTER);
            document.add(terms);

        } catch (DocumentException e) {
            throw new Exception("Error generating PDF: " + e.getMessage(), e);
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return out.toByteArray();
    }

    // Helper method to create cell with label-value format
    private static PdfPCell createLabelValueCell(String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph labelPara = new Paragraph(label, labelFont);
        Paragraph valuePara = new Paragraph(value, valueFont);

        cell.addElement(labelPara);
        cell.addElement(valuePara);

        return cell;
    }

    // Generate a order number based on order details
    private static String generateOrderNumber(OrderDetail orderDetail) {
        // Use actual order ID if available, otherwise create a placeholder
        if (orderDetail.getOrderId() != null) {
            return String.format("%08d", orderDetail.getOrderId());
        } else {
            // Create a timestamp-based order number if ID is not available
            return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        }
    }
}