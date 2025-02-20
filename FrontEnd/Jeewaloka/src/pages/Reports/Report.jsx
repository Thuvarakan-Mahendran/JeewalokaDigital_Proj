import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select";

export default function ReportsPage() {
  const [reportType, setReportType] = useState("");
  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold">Reports</h1>
      <Card className="mt-4 p-6 w-full max-w-2xl">
        <CardContent>
          <Select onValueChange={(value) => setReportType(value)}>
            <SelectTrigger className="w-64">
              <SelectValue placeholder="Report type" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="sales">Sales Report</SelectItem>
              <SelectItem value="inventory">Inventory Report</SelectItem>
              <SelectItem value="finance">Financial Report</SelectItem>
            </SelectContent>
          </Select>
          <div className="mt-6 space-y-4">
            <div className="flex justify-between items-center">
              <span>This month's total cash sale by now:</span>
              <span className="font-semibold">$10,000</span>
            </div>
            <div className="flex justify-between items-center">
              <span>This month's total credit sale by now:</span>
              <span className="font-semibold">$5,000</span>
            </div>
            <div className="flex justify-between items-center">
              <span>This month's total sale by now:</span>
              <span className="font-semibold">$15,000</span>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
